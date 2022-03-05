package com.example.oncart.networkServices

sealed class Result<T> {
    data class Success<T>(val body: T, val code: Int) : Result<T>()
    data class Failure<T>(val errorMessage: String, val code: Int) : Result<T>()
}