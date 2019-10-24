package com.cognitev.task.remote.responses

import com.cognitev.task.model.Venue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchBaseResponse {

    @SerializedName("meta")
    @Expose
    var meta:MetaResponse? = null

    @SerializedName("response")
    @Expose
    var response:SearchResponse? = null
}
