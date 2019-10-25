package com.cognitev.task.view.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cognitev.task.R
import com.cognitev.task.model.Venue
import com.cognitev.task.model.VenuePhoto
import com.cognitev.task.remote.repository.VenuesRepository
import com.cognitev.task.view.activity.BaseActivity
import com.cognitev.task.viewmodel.VenuesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class VenueRecyclerAdapter() : RecyclerView.Adapter<VenueRecyclerAdapter.VenueViewHolder>(){

    var activity:BaseActivity? = null
    var venuesList:List<Venue> = arrayListOf()
    lateinit var venueViewModel: VenuesViewModel

    constructor(activity: BaseActivity, venuesList:List<Venue>):this(){
        this.activity = activity
        this.venuesList = venuesList
        venueViewModel = ViewModelProviders.of(activity).get(VenuesViewModel::class.java)
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

        if(!venueViewModel.getVenuesBitmap().containsKey(venue.id)) {
            val disposable = VenuesRepository.getInstance(activity!!)
                .getVenuePhoto(SimpleDateFormat("YYYYMMdd").format(Date()), venue.id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        Log.e(TAG, "photoResponse: ${venue.id}")
                        if (response.body()!!.meta!!.code == 200) {
                            if (response.body()!!.response!!.photosArrayResponse!!.count > 0) {
                                comprisePhotoAndLoad(
                                    response.body()!!.response!!.photosArrayResponse!!.photos!![0],
                                    holder.venueImage,
                                    venue.id!!
                                )
                            }
                        }
                    },
                    {
                        Log.e(TAG, "photoException: ${it.message}")
                    }
                )
            activity!!.disposables.add(disposable)
        }else{
            holder.venueImage.setImageBitmap(venueViewModel.getVenuesBitmap()[venue.id])
            Log.e(TAG, "venueImage: alreadyFetched + ${venue.id}")
        }
    }

    private fun comprisePhotoAndLoad(venuePhoto: VenuePhoto, venueImage: ImageView, venueId:String) {
        val imageUrl = venuePhoto.prefix.plus("300x200").plus(venuePhoto.suffix)
        Glide.with(activity!!.applicationContext)
            .asBitmap()
            .load(imageUrl)
            .into(object: CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    venueViewModel.getVenuesBitmap()[venueId] = resource
                    venueImage.setImageBitmap(resource)
                }
            })
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