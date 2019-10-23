package com.cognitev.task.remote

import com.cognitev.task.remote.responses.VenuesSearchResponse
import com.cognitev.task.utils.Constants
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit

interface ApiService{

    //api call to get places
    @GET("search")
    fun getVenuesByLocation(@Query("client_id") clientId:String,
                            @Query("client_secret") clientSecret:String,
                            @Query("v") version:String,
                            @Query("ll") location:String):Observable<Response<VenuesSearchResponse>>

    companion object{
        fun create(): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.API_BASE_URL)
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}