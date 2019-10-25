package com.cognitev.task.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cognitev.task.model.Venue


@Dao
interface VenueDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVenue(venue: Venue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertVenues(venue: List<Venue>)

    @Query("DELETE FROM venue_table")
    fun deleteAllVenues()

    @Query("SELECT * from venue_table")
    fun getAllVenues():LiveData<List<Venue>>

    @Update
    fun updateVenue(venue: Venue)

    @Query("UPDATE venue_table SET photoUrl=:photoUrl WHERE id=:id")
    fun updateVenuePhotoUrl(id:String, photoUrl:String)

    @Update
    fun updateVenues(vararg venues:Venue)
}