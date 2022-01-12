package com.clover.rickandmorty.api

import com.clover.rickandmorty.model.LocationDetail
import com.clover.rickandmorty.model.Characters
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("character/{ids}")
    suspend fun getCharacters (@Path("ids") ids: List<Int>): Characters

    @GET("location/{id}")
    suspend fun getLocation (@Path("id") id: Int): LocationDetail

}