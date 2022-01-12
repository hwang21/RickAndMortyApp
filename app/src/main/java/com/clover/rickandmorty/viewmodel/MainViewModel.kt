package com.clover.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.clover.rickandmorty.api.RetrofitInstance
import com.clover.rickandmorty.model.LocationDetail
import com.clover.rickandmorty.repository.Repository
import com.clover.rickandmorty.utils.Resource

import kotlinx.coroutines.Dispatchers

class MainViewModel(application: Application): AndroidViewModel(application) {

    val imageUrl = MutableLiveData<String>()

    val locationId = MutableLiveData<Int>()

    fun setImage(_imageUrl: String) {
        imageUrl.value = _imageUrl
    }

    fun setLocationId(_locationId: Int) {
        locationId.value = _locationId
    }

    fun getCharacters() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val characters = Repository(RetrofitInstance.apiService).getCharacters()
            emit(Resource.success(data = characters))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getLocationDetails(locationId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val location: LocationDetail = Repository(RetrofitInstance.apiService).getLocation(locationId)
            emit(Resource.success(data = location))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}