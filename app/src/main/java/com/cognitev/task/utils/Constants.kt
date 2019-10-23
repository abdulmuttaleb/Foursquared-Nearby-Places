package com.cognitev.task.utils

object Constants{

    const val PREF_NAME = "local_pref"
    const val PREF_MODE_KEY = "operation_mode"
    const val MODE_REALTIME = "realtime"
    const val MODE_SINGLE = "single_update"

    //Permissions constants
    val LOCATION_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION)
    const val LOCATION_REQUEST = 1
}