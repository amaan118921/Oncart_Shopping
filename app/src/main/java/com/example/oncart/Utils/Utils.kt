package com.example.oncart.Utils

import android.app.Activity
import android.widget.Toast
import com.example.oncart.helper.toProductItems
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems
import kotlinx.coroutines.*
import java.lang.Exception
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
        suspend fun getFilteredList(query: String?, list: ArrayList<ProductItems>): ArrayList<ProductItems> =
            withContext(Dispatchers.Default) {
                 try {
                    val filtered = arrayListOf<ProductItems>()
                    list.forEach {
                        if(query?.let { it1 -> it.id.contains(it1, true) }==true) {
                            filtered.add(it)
                        }
                    }
                     return@withContext filtered
                }catch (e: Exception) {
                     return@withContext listOf<ProductItems>() as ArrayList<ProductItems>
                }
            }
        }
}