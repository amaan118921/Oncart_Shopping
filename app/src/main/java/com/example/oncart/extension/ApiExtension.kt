package com.example.oncart.extension

import com.example.oncart.networkServices.Result
import kotlinx.coroutines.Deferred
import retrofit2.Response


suspend fun <T> Deferred<Response<T>>.get(): Result<T> {
    val response = await()
    return if(response.isSuccessful) {
        Result.Success(response.body(), response.code()) as Result<T>
    }else {
        Result.Failure(response.errorBody().toString(), response.code())
    }
}