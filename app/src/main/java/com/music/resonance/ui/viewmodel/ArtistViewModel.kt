package com.music.resonance.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.resonance.data.model.Artist
import com.music.resonance.data.network.ArtistClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ArtistViewModel : ViewModel(){
    var artist by mutableStateOf<Response<Artist>?>(null)
        private set

    private var _artists = MutableStateFlow<Response<List<Artist>>?>(null)
    var artists: StateFlow<Response<List<Artist>>?> = _artists.asStateFlow()

    var loading by mutableStateOf(false)
        private set

    fun getArtists(){
        viewModelScope.launch {
            loading = true
            try{
                _artists.value = ArtistClient.apiService.getArtist()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getArtistById(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                artist = ArtistClient.apiService.getArtistById(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun addArtist(newArtist: Artist){
        viewModelScope.launch {
            loading = true
            try{
                artist = ArtistClient.apiService.addArtist(newArtist)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun updArtist(id: Long, updArtist: Artist){
        viewModelScope.launch {
            loading = true
            try{
                artist = ArtistClient.apiService.updArtist(id, updArtist)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun delArtist(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                ArtistClient.apiService.delArtist(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }
}