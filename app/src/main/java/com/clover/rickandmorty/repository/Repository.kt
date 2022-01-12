package com.clover.rickandmorty.repository

import com.clover.rickandmorty.api.ApiService
import kotlinx.coroutines.flow.flow

class Repository(private val apiService: ApiService) {

    fun getCharacters() = flow {
        emit(apiService.getCharacters(List(20) { (it + 1) }))
    }

    fun getLocation(id: Int) = flow {
        emit(apiService.getLocation(id))
    }

}