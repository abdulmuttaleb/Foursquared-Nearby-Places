package com.cognitev.task.model

import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NonNls

@Entity(tableName = "venue_table")
data class Venue(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    @SerializedName("id")
    @Expose
    var id:String,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    var name:String? = null,

    @Embedded
    @SerializedName("location")
    @Expose
    var location:VenueLocation? = null,

    @ColumnInfo(name = "categories")
    @SerializedName("categories")
    @Expose
    var categories:List<Category>? = null,

    @ColumnInfo(name = "photoUrl")
    var photoUrl:String? = null
)
