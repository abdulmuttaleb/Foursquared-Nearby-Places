package com.cognitev.task.remote.repository

import android.content.Context
import com.cognitev.task.BuildConfig
import com.cognitev.task.remote.ApiService
import com.cognitev.task.remote.responses.SearchBaseResponse
import com.cognitev.task.utils.SingletonHolder
import io.reactivex.Observable
import retrofit2.Response

class VenuesRepository(context: Context) : BaseRepository() {

    private var apiService = ApiService.create()

    fun getVenuesByLocation(version: String, location: String)
            = getVenuesByLocationCall(version, location)

    private fun getVenuesByLocationCall(version:String, location:String):Observable<Response<SearchBaseResponse>>{
       return apiService.getVenuesByLocation(
           BuildConfig.FOURSQUARED_CLIENT_ID,
           BuildConfig.FOURSQUARED_CLIENT_SECRET,
           version,
           location)
    }

    companion object : SingletonHolder<VenuesRepository, Context>(::VenuesRepository)
}