package com.example.oncart.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionReceiver: BroadcastReceiver() {
    var listener: ReceiverListener? = null
    override fun onReceive(context: Context?, p1: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        if(listener!=null) {
            val isConnected = networkInfo!=null && networkInfo.isConnectedOrConnecting
            listener?.onNetworkChange(isConnected)
        }
    }


    interface ReceiverListener {
        fun onNetworkChange(isConnected: Boolean)
    }
}