package com.cognitev.task.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("id")
    @Expose
    var id:String? = null,

    @SerializedName("name")
    @Expose
    var name:String? = null,

    @SerializedName("location")
    @Expose
    var location:VenueLocation? = null,

    @SerializedName("categories")
    @Expose
    var categories:List<Category>? = null
)
