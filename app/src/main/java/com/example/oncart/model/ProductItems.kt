package com.example.oncart.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class ProductItems(
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "imageUrl") @NonNull var imageUrl: String = "",
    @ColumnInfo(name = "price")@NonNull var price: String = "",
    @ColumnInfo(name = "timeStamp")@NonNull var timeStamp: Long = 0,
    @ColumnInfo(name = "quantity")var quantity: Int? = null
): Parcelable

data class ProductModel (
    var dataArray: List<ProductItems>
        )

@Parcelize
@Entity
data class LikedItems(
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "imageUrl") @NonNull var imageUrl: String = "",
    @ColumnInfo(name = "price")@NonNull var price: String = "",
    @ColumnInfo(name = "timeStamp")@NonNull var timeStamp: Long = 0,
    @ColumnInfo(name = "quantity")var quantity: Int? = null
): Parcelable

data class LikedModel (
    var dataArray: List<LikedItems>
)

data class ProductItemModel(
    var productItem: ProductItems
)
