package com.cognitev.task.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.cognitev.task.R
import com.cognitev.task.util.Constants

class MainActivity : BaseActivity(){

    lateinit var typeSwitchTextView: TextView
    lateinit var placesRecyclerView: RecyclerView

    lateinit var sharedPreferences:SharedPreferences

    var operationalMode: MutableLiveData<String> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityUiInit()
    }

    fun activityUiInit(){
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

        operationalMode.postValue(sharedPreferences.getString(Constants.PREF_MODE_KEY, Constants.MODE_REALTIME))

        operationalMode.observe(this, Observer {
            Log.e(TAG, "operationModeObserver: $it")
            when(it){
                Constants.MODE_REALTIME ->{
                    typeSwitchTextView.text = "Realtime"
                }

                Constants.MODE_SINGLE->{
                    typeSwitchTextView.text = "Single Update"
                }
            }
        })
        typeSwitchTextView = findViewById(R.id.tv_type_switch)
        placesRecyclerView = findViewById(R.id.rv_places)

        //init click functionality
        typeSwitchTextView.setOnClickListener {
           switchMode()
        }

        //init connectivity observer
        isOnline.observe(this, Observer {
            Log.e(TAG, "connection: $it")
        })
    }

    fun switchMode(){
        val editor =  sharedPreferences.edit()
        when(operationalMode.value){
            Constants.MODE_SINGLE ->{
                editor.putString(Constants.PREF_MODE_KEY, Constants.MODE_REALTIME)
                editor.apply()
                operationalMode.postValue(Constants.MODE_REALTIME)
            }

            Constants.MODE_REALTIME ->{
                editor.putString(Constants.PREF_MODE_KEY, Constants.MODE_SINGLE)
                editor.apply()
                operationalMode.postValue(Constants.MODE_SINGLE)
            }
        }
    }

    companion object{
        const val TAG = "MainActivity"
    }
}