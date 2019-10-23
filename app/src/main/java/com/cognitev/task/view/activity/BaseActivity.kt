package com.cognitev.task.view.activity

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.cognitev.task.MyApplication
import com.cognitev.task.utils.receivers.ConnectivityReceiver
import io.reactivex.disposables.Disposable


open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    var disposables: ArrayList<Disposable> = arrayListOf()
    var isOnline = MutableLiveData<Boolean>().apply { value = false }

    override fun onPause() {
        super.onPause()
        if(disposables.isNotEmpty()) {
            disposables.forEach {
                it.dispose()
            }
        }
        MyApplication.instance!!.removeConnectivityListener()
    }

    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        isOnline.postValue(isConnected)
    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.setConnectivityListener(this)
    }
}