package com.cognitev.task.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cognitev.task.model.Venue


@Dao
interface VenueDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVenue(venue: Venue)

    @Query("DELETE FROM venue_table")
    fun deleteAllVenues()

    @Query("SELECT * from venue_table")
    fun getAllVenues():LiveData<List<Venue>>

    @Update
    fun updateVenue(venue: Venue)

    @Update
    fun updateVenues(vararg venues:Venue)
}