package com.music.resonance.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.resonance.data.model.Album
import com.music.resonance.data.network.AlbumClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AlbumViewModel : ViewModel() {
    var album by mutableStateOf<Response<Album>?>(null)
        private set

    private var _albums = MutableStateFlow<Response<List<Album>>?>(null)
    var albums: StateFlow<Response<List<Album>>?> = _albums.asStateFlow()
        private set

    var loading by mutableStateOf(false)
        private set

    fun getAlbums(){
        viewModelScope.launch {
            loading = true
            try{
                _albums.value = AlbumClient.apiService.getAlbums()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getAlbumById(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                album = AlbumClient.apiService.getAlbumById(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getAlbumByTitle(title: String){
        viewModelScope.launch {
            loading = true
            try{
                _albums.value = AlbumClient.apiService.getAlbumByTitle(title)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun addAlbum(newAlbum: Album){
        viewModelScope.launch {
            loading = true
            try{
                album = AlbumClient.apiService.addAlbum(newAlbum)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun updAlbum(id: Long, updAlbum: Album){
        viewModelScope.launch {
            loading = true
            try{
                album = AlbumClient.apiService.updAlbum(id, updAlbum)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun delAlbum(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                AlbumClient.apiService.delAlbum(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun addMusicToAlbum(idAlbum: Long, idMusic: Long){
        viewModelScope.launch {
            loading = true
            try{
                album = AlbumClient.apiService.addMusicToAlbum(idAlbum, idMusic)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun delMusicFromAlbum(idAlbum: Long, idMusic: Long){
        viewModelScope.launch {
            loading = true
            try{
                album = AlbumClient.apiService.delMusicFromAlbum(idAlbum, idMusic)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }
}