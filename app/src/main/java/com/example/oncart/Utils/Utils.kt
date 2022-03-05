package com.example.oncart.Utils

import android.app.Activity
import android.widget.Toast
import com.example.oncart.helper.toProductItems
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class Utils {
    companion object {
        fun showToast(activity: Activity, msg: String) {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }
        fun List<LikedItems>.toListOfProductItems(): List<ProductItems> {
            val list = mutableListOf<ProductItems>()
            runBlocking {
                launch(Dispatchers.Default) {
                    this@toListOfProductItems.forEach {
                        list.add(it.toProductItems())
                    }
                }
            }
            return list
        }
    }
}