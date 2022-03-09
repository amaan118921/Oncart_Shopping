package com.example.oncart.eventBus

import com.example.oncart.model.ProductItems

class ProductEvent(
    private val listStr: String, val list: List<ProductItems>
): MessageEvent(listStr) {
    fun getListOfProductItems(): List<ProductItems> = list
}
