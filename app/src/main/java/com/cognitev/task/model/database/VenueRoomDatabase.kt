package com.cognitev.task.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.cognitev.task.model.Venue
import com.cognitev.task.model.dao.VenueDao
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import androidx.room.Room
import androidx.room.TypeConverters
import com.cognitev.task.utils.Converters
import com.cognitev.task.utils.SingletonHolder


@Database(entities = [Venue::class], version = 1)
@TypeConverters(Converters::class)
abstract class VenueRoomDatabase : RoomDatabase(){

    abstract fun venueDao():VenueDao

    companion object : SingletonHolder<VenueRoomDatabase, Context>({
        Room.databaseBuilder(it, VenueRoomDatabase::class.java, "venues_database").build()
    })
}