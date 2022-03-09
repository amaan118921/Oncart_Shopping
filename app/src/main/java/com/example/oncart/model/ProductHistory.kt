package com.example.oncart.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class ProductHistory(
    @ColumnInfo(name="productHistory") @NonNull var productHistory: List<ProductItems>
)
