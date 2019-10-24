package com.cognitev.task.remote.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MetaResponse {

    @SerializedName("code")
    @Expose
    var code:Int? = null

    @SerializedName("requestId")
    @Expose
    var requestId:String? = null

}
