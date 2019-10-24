package com.cognitev.task.remote.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPhotoResponse {
    @SerializedName("photos")
    @Expose
    var photosArrayResponse: PhotosArrayResponse? = null
}
