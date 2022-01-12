package com.clover.rickandmorty.repository

import com.clover.rickandmorty.api.ApiService

class Repository(private val apiService: ApiService) {

    suspend fun getCharacters() = apiService.getCharacters(List(20) { (it + 1) })

    suspend fun getLocation(id: Int) = apiService.getLocation(id)

}