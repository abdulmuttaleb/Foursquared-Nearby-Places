package com.cognitev.task

import android.app.Application
import android.net.ConnectivityManager
import android.content.IntentFilter
import android.os.Build
import com.cognitev.task.utils.ConnectivityReceiver


class MyApplication : Application() {
    internal var connectivityReceiver = ConnectivityReceiver()
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                connectivityReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    fun removeConnectivityListener() {
        unregisterReceiver(connectivityReceiver)
    }

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }
}