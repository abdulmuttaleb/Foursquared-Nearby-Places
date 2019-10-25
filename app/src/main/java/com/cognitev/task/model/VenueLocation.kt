package com.cognitev.task.model

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VenueLocation (
    @SerializedName("address")
    @Expose
    var address:String? = null,

    @SerializedName("crossStreet")
    @Expose
    var crossStreet:String? = null,

    @SerializedName("formattedAddress")
    @Expose
    var formattedAddress:List<String>? = null

)
