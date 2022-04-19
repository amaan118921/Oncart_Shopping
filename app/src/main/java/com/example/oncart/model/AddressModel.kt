package com.example.oncart.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddressModel(
    @PrimaryKey var id: Long? = null, @ColumnInfo(name="name") @NonNull var name: String = "", @ColumnInfo(name="address") @NonNull var address: String = "",
    @ColumnInfo(name="phone") @NonNull var phone: String = ""
)