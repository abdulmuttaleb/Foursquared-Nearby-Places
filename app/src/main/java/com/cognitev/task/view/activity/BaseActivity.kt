package com.cognitev.task.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cognitev.task.R
import com.cognitev.task.utils.ConnectivityReceiver
import io.reactivex.disposables.Disposable


open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    var disposables: ArrayList<Disposable?>? = null
    var isOnline = MutableLiveData<Boolean>().apply { value = false }

    override fun onPause() {
        super.onPause()
        disposables!!.forEach {
            it!!.dispose()
        }
    }

    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        isOnline.postValue(isConnected)
    }
}