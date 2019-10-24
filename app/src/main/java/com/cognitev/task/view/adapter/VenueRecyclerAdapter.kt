package com.cognitev.task.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cognitev.task.R
import com.cognitev.task.model.Venue
import com.cognitev.task.model.VenuePhoto
import com.cognitev.task.remote.repository.VenuesRepository
import com.cognitev.task.view.activity.BaseActivity
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

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

        val disposable = VenuesRepository.getInstance(activity!!)
            .getVenuePhoto(SimpleDateFormat("YYYYMMdd").format(Date()), venue.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response->
                    Log.e(TAG, "photoResponse: ${response.body()!!.response}")
                    if(response.body()!!.meta!!.code == 200){
                        if(response.body()!!.response!!.photosArrayResponse!!.count > 0) {
                            comprisePhotoAndLoad(
                                response.body()!!.response!!.photosArrayResponse!!.photos!![0],
                                holder.venueImage)
                        }
                    }
                },
                {
                    Log.e(TAG, "photoException: ${it.message}")
                }
            )
        activity!!.disposables.add(disposable)
    }

    private fun comprisePhotoAndLoad(venuePhoto: VenuePhoto, venueImage: ImageView) {
        val imageUrl = venuePhoto.prefix.plus("300x200").plus(venuePhoto.suffix)
        Picasso.get().load(imageUrl)
            .resize(300,200)
            .centerCrop()
            .placeholder(activity!!.resources.getDrawable(R.drawable.ic_photo))
            .into(venueImage)
    }

    inner class VenueViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var venueImage: ImageView = itemView.findViewById(R.id.iv_image)
        var venueName:TextView = itemView.findViewById(R.id.tv_place_name)
        var venueDescription:TextView = itemView.findViewById(R.id.tv_place_description)
    }

    companion object{
        const val TAG = "VenueAdapter"
    }

}