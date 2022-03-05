package com.example.oncart.networkServices

import com.example.oncart.helper.Constants
import com.example.oncart.model.LikedModel
import com.example.oncart.model.ProductItemModel
import com.example.oncart.model.ProductItems
import com.example.oncart.model.ProductModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
        Constants.BASE_URL
    ).build()

private val retro = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
    Constants.BASE_URL
).build()

interface Products {
    @GET("api/{product}")
    suspend fun getMobiles(@Path("product") product: String): ProductModel
}

object ProductAPI {
    val retrofitService: Products by lazy { retrofit.create(Products::class.java) }
}

object API {
    val retrofitService: Products by lazy { retro.create(Products::class.java) }
}
