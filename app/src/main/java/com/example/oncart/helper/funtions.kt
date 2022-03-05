package com.example.oncart.helper

import android.view.View
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun ProductItems.toLikedItems(): LikedItems {
    return LikedItems(id, imageUrl, price, timeStamp, quantity)
}

fun ProductItems.default() : ProductItems {
    return ProductItems(id, imageUrl, price, timeStamp, 1)
}

fun LikedItems.toProductItems(): ProductItems {
    return ProductItems(id, imageUrl, price, timeStamp, quantity)
}






