package com.music.resonance.data.network

import com.music.resonance.data.model.Artist
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ArtistAPIService{
    @GET("")
    suspend fun getArtist(): Response<List<Artist>>

    @GET("{id}")
    suspend fun getArtistById(@Path("id") id: Long): Response<Artist>

    @POST("")
    suspend fun addArtist(@Body artist: Artist): Response<Artist>

    @PUT("{id}")
    suspend fun updArtist(@Path("id") id: Long, @Body artist: Artist): Response<Artist>

    @DELETE("{id}")
    suspend fun delArtist(@Path("id") id: Long): Response<Unit>
}

object ArtistClient{
    private const val BASE_URL = "http://10.0.2.2:8081/artists/"

    val apiService: ArtistAPIService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtistAPIService::class.java)
    }
}