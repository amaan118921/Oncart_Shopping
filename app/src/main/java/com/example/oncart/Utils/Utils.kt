package com.example.oncart.Utils

import android.app.Activity
import android.widget.Toast
import com.example.oncart.model.ProductItems

class Utils {
    companion object {
        fun showToast(activity: Activity, msg: String) {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }
    }


}