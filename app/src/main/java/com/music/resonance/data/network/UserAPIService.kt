package com.music.resonance.data.network

import com.music.resonance.data.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPIService{
    @GET("")
    suspend fun getUsers(): Response<List<User>>

    @GET("{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>

    @GET("user")
    suspend fun getUserByName(@Query("name") name: String): Response<User>

    @POST("")
    suspend fun addUser(@Body user: User): Response<User>

    @PUT("{id}")
    suspend fun updUser(@Path("id") id: Long, @Body user: User): Response<User>

    @DELETE("{id}")
    suspend fun delUser(@Path("id") id: Long): Response<Unit>
}

object UserClient {
    private const val BASE_URL = "https://resonance-api-bfax.onrender.com/users/"

    val apiService: UserAPIService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPIService::class.java)
    }
}