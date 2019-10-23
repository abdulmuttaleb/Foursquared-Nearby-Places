package com.cognitev.task.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.cognitev.task.R

class MainActivity : BaseActivity(){

    lateinit var typeSwitchTextView: TextView
    lateinit var placesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityUiInit()
    }

    fun activityUiInit(){

        typeSwitchTextView = findViewById(R.id.tv_type_switch)
        placesRecyclerView = findViewById(R.id.rv_places)

        //init connectivity observer
        isOnline.observe(this, Observer {
            Log.e(TAG, "connection: $it")
        })
    }

    companion object{
        const val TAG = "MainActivity"
    }
}