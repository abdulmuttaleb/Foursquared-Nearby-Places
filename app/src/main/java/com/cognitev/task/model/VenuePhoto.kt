package com.cognitev.task.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VenuePhoto(
    @SerializedName("prefix")
    @Expose
    var prefix:String? = null,

    @SerializedName("suffix")
    @Expose
    var suffix:String? = null
)