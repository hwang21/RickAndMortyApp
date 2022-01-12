package com.clover.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.clover.rickandmorty.api.RetrofitInstance
import com.clover.rickandmorty.model.Characters
import com.clover.rickandmorty.model.LocationDetail
import com.clover.rickandmorty.repository.Repository
import com.clover.rickandmorty.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _charactersState = MutableStateFlow<ResponseState<Characters>>(ResponseState.Empty)

    internal val charactersState: StateFlow<ResponseState<Characters>> = _charactersState

    private val _locationState = MutableStateFlow<ResponseState<LocationDetail>>(ResponseState.Empty)

    internal val locationState: StateFlow<ResponseState<LocationDetail>> = _locationState

    internal val imageUrl = MutableStateFlow("")

    internal val locationId = MutableStateFlow(0)

    fun getImageStateFlow(): StateFlow<String> = imageUrl

    fun getLocationIdStateFlow(): StateFlow<Int> = locationId

    fun getCharacters() = viewModelScope.launch {
        _charactersState.value = ResponseState.Loading
        Repository(RetrofitInstance.apiService).getCharacters()
            .flowOn(Dispatchers.IO)
            .catch { e ->
                _charactersState.value = ResponseState.Error(e.toString())
            }
            .collect {
                _charactersState.value = ResponseState.Success(it)
            }
    }

    fun getLocationDetails(locationId: Int) = viewModelScope.launch {
        _locationState.value = ResponseState.Loading
        Repository(RetrofitInstance.apiService).getLocation(locationId)
            .flowOn(Dispatchers.IO)
            .catch { e ->
                _locationState.value = ResponseState.Error(e.toString())
            }
            .collect {
                _locationState .value = ResponseState.Success(it)
            }
    }
}