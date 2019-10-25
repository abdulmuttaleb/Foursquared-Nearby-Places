package com.cognitev.task.utils

import androidx.room.TypeConverter
import com.cognitev.task.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {

    @TypeConverter
    fun fromString(value: String): ArrayList<Category> {
        val listType = object : TypeToken<ArrayList<Category>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Category>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}