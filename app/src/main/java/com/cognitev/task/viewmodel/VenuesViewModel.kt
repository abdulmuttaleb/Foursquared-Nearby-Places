package com.cognitev.task.viewmodel

import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cognitev.task.model.Venue
import com.cognitev.task.remote.repository.VenuesRepository
import com.cognitev.task.view.activity.BaseActivity
import com.cognitev.task.view.activity.MainActivity
import com.cognitev.task.view.adapter.VenueRecyclerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class VenuesViewModel(var activity: BaseActivity) :ViewModel(){
    private var venuesList: MutableLiveData<List<Venue>> = MutableLiveData(arrayListOf())
    private var venuesBitmapList: MutableMap<String, Bitmap> = mutableMapOf()
    private var firstTimeLoading:Boolean = true
    private var location:Location? = null

    fun getVenuesLiveData():MutableLiveData<List<Venue>>{
        return venuesList
    }

    fun getVenuesBitmap():MutableMap<String, Bitmap>{
        return venuesBitmapList
    }

    fun setLocation(location: Location){
        this.location = location
        Log.e(TAG, "setLocation: ${this.location}")
    }

    fun getLocation():Location?{
        return location
    }

    fun fetchVenues(location:Location, venueRecyclerAdapter:VenueRecyclerAdapter, loadingView:View){
        if(firstTimeLoading){
            loadingView.visibility = View.VISIBLE
            firstTimeLoading = false
        }
        val version = SimpleDateFormat("YYYYMMdd").format(Date())
        val tempLocation = location.latitude.toString().plus(", ").plus(location.longitude)
        Log.e(MainActivity.TAG, "version: $version")
        Log.e(MainActivity.TAG, "location: $tempLocation")
        val disposable = VenuesRepository.getInstance(activity.applicationContext)
            .getVenuesByLocation(version, tempLocation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e(MainActivity.TAG, "response: ${it.body()!!.response!!.venues}")
                    val venues = it.body()!!.response!!.venues
                    venuesList.postValue(venues!!)
                    venueRecyclerAdapter.notifyDataSetChanged()

                    if(loadingView.visibility == View.VISIBLE){
                        loadingView.visibility = View.GONE
                    }
                },
                {
                    Log.e(MainActivity.TAG, "getVenuesException: $it")
                    if(loadingView.visibility == View.VISIBLE){
                        loadingView.visibility = View.GONE
                    }
                }
            )
        activity.disposables.add(disposable)
    }

    companion object{
        const val TAG = "VenuesViewModel"
    }
}