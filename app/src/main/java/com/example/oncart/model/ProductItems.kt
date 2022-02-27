package com.example.oncart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductItems(
    var name: String = "", var imageUrl: String = "", var price: String = ""
): Parcelable

data class ProductModel (
    var dataArray: List<ProductItems>
        )

