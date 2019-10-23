package com.cognitev.task.remote.responses

import com.cognitev.task.model.Venue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VenuesSearchResponse {

    @SerializedName("venues")
    @Expose
    var venues:List<Venue>? = null
}
