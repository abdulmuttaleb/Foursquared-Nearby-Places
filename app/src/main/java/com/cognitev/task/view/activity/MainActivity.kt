package com.cognitev.task.view.activity

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.cognitev.task.R
import com.cognitev.task.utils.Constants
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import org.json.JSONObject
import com.google.android.gms.location.LocationServices
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import com.cognitev.task.model.VenueLocation
import com.cognitev.task.remote.repository.VenuesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : BaseActivity(){

    lateinit var typeSwitchTextView: TextView
    lateinit var placesRecyclerView: RecyclerView
    lateinit var emptyDataView:View
    lateinit var noConnectionView:View
    lateinit var loadingView:View

    lateinit var sharedPreferences:SharedPreferences

    var operationalMode: MutableLiveData<String> = MutableLiveData()

    //location vars
    lateinit var googleApiClient:GoogleApiClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest:LocationRequest

    var firstTimeLoading:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        activityInit()
    }

    fun activityInit(){
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

        operationalMode.postValue(sharedPreferences.getString(Constants.PREF_MODE_KEY, Constants.MODE_REALTIME))

        initLocationVars()

        operationalMode.observe(this, Observer {
            Log.e(TAG, "operationModeObserver: $it")
            when(it){
                Constants.MODE_REALTIME ->{
                    typeSwitchTextView.text = "Realtime"
                    registerLocationTracking()
                }

                Constants.MODE_SINGLE->{
                    typeSwitchTextView.text = "Single Update"
                    unregisterLocationTracking()
                    getOneshotLocation()
                }
            }
        })

        // init ui vars
        typeSwitchTextView = findViewById(R.id.tv_type_switch)
        placesRecyclerView = findViewById(R.id.rv_places)
        emptyDataView = findViewById(R.id.cl_empty)
        noConnectionView = findViewById(R.id.cl_no_connection)
        loadingView = findViewById(R.id.cl_loading)

        //init click functionality
        typeSwitchTextView.setOnClickListener {
           switchMode()
        }

        //init connectivity observer
        isOnline.observe(this, Observer {
            Log.e(TAG, "connection: $it")
            when(it){
                true ->{
                    noConnectionView.visibility = View.GONE
                    placesRecyclerView.visibility = View.VISIBLE
                }

                false ->{
                    noConnectionView.visibility = View.VISIBLE
                    placesRecyclerView.visibility = View.GONE
                    emptyDataView.visibility = View.GONE
                }
            }
        })
    }

    fun switchMode(){
        val editor =  sharedPreferences.edit()
        when(operationalMode.value){
            Constants.MODE_SINGLE ->{
                editor.putString(Constants.PREF_MODE_KEY, Constants.MODE_REALTIME)
                editor.apply()
                operationalMode.postValue(Constants.MODE_REALTIME)
            }

            Constants.MODE_REALTIME ->{
                editor.putString(Constants.PREF_MODE_KEY, Constants.MODE_SINGLE)
                editor.apply()
                operationalMode.postValue(Constants.MODE_SINGLE)
            }
        }
    }


    fun getOneshotLocation(){

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.e(TAG, "lastLocation: $location")
            if (location != null) {
                compriseApiCall(location)
            }
        }
    }

    fun initLocationVars() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest()
            .setSmallestDisplacement(Constants.DISTANCE_TO_UPDATE)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        googleApiClient = GoogleApiClient.Builder(applicationContext)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()

        locationCallback = object:LocationCallback(){
            override fun onLocationResult(result: LocationResult?) {
                result ?: return
                for(location in result.locations){
                    Log.e(TAG, "result : ${location.latitude} and ${location.longitude}")
                    /**
                    Here I will get the regular location updates depending on the location request criteria I provided
                     */
                    compriseApiCall(location)
                }
            }
        }


    }

    fun unregisterLocationTracking(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun registerLocationTracking(){
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    fun compriseApiCall(location:Location){
        if(firstTimeLoading){
            loadingView.visibility = View.VISIBLE
            firstTimeLoading = false
        }
        val version = SimpleDateFormat("YYYYMMdd").format(Date())
        val tempLocation = location.latitude.toString().plus(", ").plus(location.longitude)
        Log.e(TAG, "version: $version")
        Log.e(TAG, "location: $tempLocation")
        val disposable = VenuesRepository.getInstance(applicationContext)
            .getVenuesByLocation(version, tempLocation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e(TAG, "response: ${it.body()!!.response!!.venues}")
                    if(loadingView.visibility == View.VISIBLE){
                        loadingView.visibility = View.GONE
                    }
                },
                {
                    Log.e(TAG, "getVenuesException: $it")
                    if(loadingView.visibility == View.VISIBLE){
                        loadingView.visibility = View.GONE
                    }
                }
            )
        disposables.add(disposable)
    }

    fun checkPermissions(){
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {
        ActivityCompat.requestPermissions(this,Constants.LOCATION_PERMISSIONS,Constants.LOCATION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.LOCATION_REQUEST) {
            if(!grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
                getLocationPermission()
            }
        }
    }
    companion object{
        const val TAG = "MainActivity"
    }
}