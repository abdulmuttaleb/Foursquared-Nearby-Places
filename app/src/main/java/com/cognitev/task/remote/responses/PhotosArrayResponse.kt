package com.cognitev.task.remote.responses

import com.cognitev.task.model.VenuePhoto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PhotosArrayResponse {
    @SerializedName("count")
    @Expose
    var count:Int = 0

    @SerializedName("items")
    @Expose
    var photos:List<VenuePhoto>? = null
}
