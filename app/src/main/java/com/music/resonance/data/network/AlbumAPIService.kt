package com.music.resonance.data.network

import com.music.resonance.data.model.Album
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

interface AlbumAPIService{
    @GET("")
    suspend fun getAlbums(): Response<List<Album>>

    @GET("{id}")
    suspend fun getAlbumById(@Path("id") id: Long): Response<Album>

    @GET("album")
    suspend fun getAlbumByTitle(@Query("title") title: String): Response<List<Album>>

    @POST("")
    suspend fun addAlbum(@Body album: Album): Response<Album>

    @PUT("{id}")
    suspend fun updAlbum(@Path("id") id: Long, @Body album: Album): Response<Album>

    @DELETE("{id}")
    suspend fun delAlbum(@Path("id") id: Long): Response<Unit>
    @POST("{id}/musics/{musicId}")
    suspend fun addMusicToAlbum(@Path("id") idAlbum: Long, @Path("musicId") idMusic: Long): Response<Album>

    @DELETE("{id}/musics/{musicId}")
    suspend fun delMusicFromAlbum(@Path("id") idAlbum: Long, @Path("musicId") idMusic: Long): Response<Album>
}

object AlbumClient{
    private const val BASE_URL = "https://resonance-api-bfax.onrender.com/albums/"

    val apiService: AlbumAPIService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbumAPIService::class.java)
    }
}