package com.cognitev.task.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("name")
    @Expose
    var name:String? = null
)