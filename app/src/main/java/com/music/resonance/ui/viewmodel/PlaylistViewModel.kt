package com.music.resonance.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.resonance.data.model.Playlist
import com.music.resonance.data.network.PlaylistClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PlaylistViewModel : ViewModel(){
    var playlist by mutableStateOf<Response<Playlist>?>(null)
        private set

    private var _playlists = MutableStateFlow<Response<List<Playlist>>?>(null)
    var playlists: StateFlow<Response<List<Playlist>>?> = _playlists.asStateFlow()

    var loading by mutableStateOf(false)
        private set

    fun clearPlaylist(){
        playlist = null
    }

    fun getPlaylists() {
        viewModelScope.launch {
            loading = true
            try{
                _playlists.value = PlaylistClient.apiService.getPlaylists()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getPlaylistById(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                playlist = PlaylistClient.apiService.getPlaylistById(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getPlaylistsByTitle(title: String){
        viewModelScope.launch {
            loading = true
            try{
                _playlists.value = PlaylistClient.apiService.getPlaylistsByName(title)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun addPlaylist(newPlaylist: Playlist){
        viewModelScope.launch {
            loading = true
            try{
                playlist = PlaylistClient.apiService.addPlaylist(newPlaylist)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun updPlaylist(id: Long, updPlaylist: Playlist){
        viewModelScope.launch {
            loading = true
            try{
                playlist = PlaylistClient.apiService.updPlaylist(id, updPlaylist)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun delPlaylist(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                PlaylistClient.apiService.delPlaylist(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun addMusicToPlaylist(idPlaylist: Long, idMusic: Long){
        viewModelScope.launch {
            loading = true
            try{
                playlist = PlaylistClient.apiService.addMusicToPlaylist(idPlaylist, idMusic)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun delMusicFromPlaylist(idPlaylist: Long, idMusic: Long){
        viewModelScope.launch {
            loading = true
            try{
                playlist = PlaylistClient.apiService.delMusicFromPlaylist(idPlaylist, idMusic)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }
}