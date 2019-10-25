package com.cognitev.task.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cognitev.task.model.Venue
import com.cognitev.task.model.dao.VenueDao

@Database(entities = [Venue::class], version = 1)
abstract class VenueRoomDatabase : RoomDatabase(){

    abstract fun venueDao():VenueDao

}