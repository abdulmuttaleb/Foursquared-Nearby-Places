package com.cognitev.task.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cognitev.task.R
import com.cognitev.task.model.Venue
import com.cognitev.task.view.activity.BaseActivity

class VenueRecyclerAdapter() : RecyclerView.Adapter<VenueRecyclerAdapter.VenueViewHolder>(){

    var activity:BaseActivity? = null
    var venuesList:List<Venue> = arrayListOf()

    constructor(activity: BaseActivity, venuesList:List<Venue>):this(){
        this.activity = activity
        this.venuesList = venuesList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        return VenueViewHolder(LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.item_place, parent, false))
    }

    override fun getItemCount(): Int {
        return venuesList.size
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val venue = venuesList[position]
        holder.venueName.text = venue.name
        holder.venueDescription.text =
            venue.categories!![0].name.plus(" - ").plus(venue.location!!.formattedAddress!![0])
    }

    inner class VenueViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var venueImage: ImageView = itemView.findViewById(R.id.iv_image)
        var venueName:TextView = itemView.findViewById(R.id.tv_place_name)
        // consists of category + location.formattedAddress[0]
        var venueDescription:TextView = itemView.findViewById(R.id.tv_place_description)
    }

}