package com.clover.rickandmorty.utils

sealed class ResponseState<out T: Any> {
    data class Success<out T: Any>(val data: T): ResponseState<T>()
    data class Error(val message: String?): ResponseState<Nothing>()
    object Loading: ResponseState<Nothing>()
    object Empty: ResponseState<Nothing>()
}