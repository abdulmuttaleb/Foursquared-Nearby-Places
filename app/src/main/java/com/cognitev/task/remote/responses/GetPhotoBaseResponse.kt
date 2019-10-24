package com.cognitev.task.remote.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPhotoBaseResponse {

    @SerializedName("meta")
    @Expose
    var meta:MetaResponse? = null

    @SerializedName("response")
    @Expose
    var response:GetPhotoResponse? = null
}