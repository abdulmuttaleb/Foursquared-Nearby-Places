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
import com.google.android.gms.location.LocationServices
import androidx.lifecycle.ViewModelProviders
import com.cognitev.task.model.Venue
import com.cognitev.task.view.adapter.VenueRecyclerAdapter
import com.cognitev.task.viewmodel.VenuesViewModel
import com.cognitev.task.viewmodel.VenuesViewModelFactory


class MainActivity : BaseActivity(){

    lateinit var typeSwitchTextView: TextView
    lateinit var placesRecyclerView: RecyclerView
    lateinit var emptyDataView:View
    lateinit var noConnectionView:View
    lateinit var loadingView:View

    lateinit var venueRecyclerAdapter:VenueRecyclerAdapter
    var venuesList:MutableList<Venue> = mutableListOf()

    lateinit var sharedPreferences:SharedPreferences

    var operationalMode: MutableLiveData<String> = MutableLiveData()

    //location vars
    lateinit var googleApiClient:GoogleApiClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest:LocationRequest

    var firstTimeLoading:Boolean = true

    //architecture vars
    lateinit var venuesViewModel:VenuesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        activityInit()
    }

    fun activityInit(){
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

        operationalMode.postValue(sharedPreferences.getString(Constants.PREF_MODE_KEY, Constants.MODE_REALTIME))

        // init ui vars
        typeSwitchTextView = findViewById(R.id.tv_type_switch)
        placesRecyclerView = findViewById(R.id.rv_places)
        emptyDataView = findViewById(R.id.cl_empty)
        noConnectionView = findViewById(R.id.cl_no_connection)
        loadingView = findViewById(R.id.cl_loading)

        venuesViewModel = ViewModelProviders.of(this, VenuesViewModelFactory(this)).get(VenuesViewModel::class.java)

        venuesViewModel.getVenuesLiveData().observe(this, Observer {
            venuesList.clear()
            venuesList.addAll(it)
        })

        venueRecyclerAdapter = VenueRecyclerAdapter(this as BaseActivity, venuesList)
        placesRecyclerView.adapter = venueRecyclerAdapter

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
                if(venuesViewModel.getVenuesLiveData().value!!.isEmpty()) {
                    venuesViewModel.fetchVenues(location, venueRecyclerAdapter, loadingView)
                    venuesViewModel.setLocation(location)
                }
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
                    /**
                    I am checking if the location value I have in the view model already is null, first creating view model,
                     or it is equal to the value already present, in case the user changes the app orientation, so it doesn't
                     have to make the call again
                     */
                    if(venuesViewModel.getLocation()== null && venuesViewModel.getLocation() != location) {
                        Log.e(TAG, "viewModelLocation: ${venuesViewModel.getLocation()}")
                        venuesViewModel.fetchVenues(location, venueRecyclerAdapter, loadingView)
                        venuesViewModel.setLocation(location)
                    }
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