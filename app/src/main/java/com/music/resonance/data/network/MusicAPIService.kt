package com.music.resonance.data.network

import com.music.resonance.data.model.Music
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MusicAPIService{
    @GET("")
    suspend fun getAllMusics(): Response<List<Music>>

    @GET("{id}")
    suspend fun getMusicById(@Path("id") id: Long): Response<Music>

    @GET("music/{title}")
    suspend fun getMusicByName(@Path("title") title: String): Response<Music>

    @POST("")
    suspend fun addMusic(@Body music: Music): Response<Music>

    @PUT("{id}")
    suspend fun updMusic(@Path("id") id: Long, @Body music: Music): Response<Music>

    @DELETE("{id}")
    suspend fun delMusic(@Path("id") id: Long): Response<Unit>
}

object MusicClient{
    private const val BASE_URL = "http://10.0.2.2:8081/musics/"

    val apiService: MusicAPIService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicAPIService::class.java)
    }
}