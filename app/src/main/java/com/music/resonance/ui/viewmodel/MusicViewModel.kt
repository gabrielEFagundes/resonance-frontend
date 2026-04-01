package com.music.resonance.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.resonance.data.model.Music
import com.music.resonance.data.network.MusicClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class MusicViewModel : ViewModel() {
    var music by mutableStateOf<Response<Music>?>(null)
        private set

    private var _musics = MutableStateFlow<Response<List<Music>>?>(null)
    var musics: StateFlow<Response<List<Music>>?> = _musics.asStateFlow()

    var loading by mutableStateOf(false)
        private set

    fun clearMusic(){
        music = null
    }

    fun getMusics() {
        viewModelScope.launch {
            loading = true
            try{
                _musics.value = MusicClient.apiService.getAllMusics()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getMusicById(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                music = MusicClient.apiService.getMusicById(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getMusicByTitle(title: String){
        viewModelScope.launch {
            loading = true
            try{
                music = MusicClient.apiService.getMusicByName(title)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun addMusic(newMusic: Music){
        viewModelScope.launch {
            loading = true
            try{
                music = MusicClient.apiService.addMusic(newMusic)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun updMusic(id: Long, updMusic: Music){
        viewModelScope.launch {
            loading = true
            try{
                music = MusicClient.apiService.updMusic(id, updMusic)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun delMusic(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                MusicClient.apiService.delMusic(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }
}