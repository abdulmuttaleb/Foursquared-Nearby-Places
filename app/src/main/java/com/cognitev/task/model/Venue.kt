package com.cognitev.task.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NonNls

@Entity(tableName = "venue_table")
data class Venue(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var id:String? = null,

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
