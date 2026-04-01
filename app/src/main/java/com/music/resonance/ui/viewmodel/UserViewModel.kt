package com.music.resonance.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.resonance.data.model.User
import com.music.resonance.data.network.UserClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel : ViewModel() {
    var user by mutableStateOf<Response<User>?>(null)
        private set

    private val _users = MutableStateFlow<Response<List<User>>?>(null)
    val users: StateFlow<Response<List<User>>?> = _users.asStateFlow()

    var loading by mutableStateOf(false)
        private set

    fun clearUser(){
        user = null
    }

    fun getUsers(){
        viewModelScope.launch {
            loading = true
            try{
                _users.value = UserClient.apiService.getUsers()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getUserById(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                user = UserClient.apiService.getUserById(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun getUserByName(name: String){
        viewModelScope.launch {
            loading = true
            try{
                user = UserClient.apiService.getUserByName(name)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun addUser(newUser: User){
        viewModelScope.launch {
            loading = true
            try{
                user = UserClient.apiService.addUser(newUser)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun updUser(id: Long, updUser: User){
        viewModelScope.launch {
            loading = true
            try{
                user = UserClient.apiService.updUser(id, updUser)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }

    fun delUser(id: Long){
        viewModelScope.launch {
            loading = true
            try{
                UserClient.apiService.delUser(id)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                loading = false
            }
        }
    }
}