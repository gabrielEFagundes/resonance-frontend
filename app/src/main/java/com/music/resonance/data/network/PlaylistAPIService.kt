package com.music.resonance.data.network

import com.google.gson.Gson
import com.music.resonance.data.model.Playlist
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaylistAPIService{
    @GET("")
    suspend fun getPlaylists(): Response<List<Playlist>>

    @GET("{id}")
    suspend fun getPlaylistById(@Path("id") id: Long): Response<Playlist>

    @GET("playlist")
    suspend fun getPlaylistsByName(@Query("title") title: String): Response<List<Playlist>>

    @POST("")
    suspend fun addPlaylist(@Body playlist: Playlist): Response<Playlist>

    @PUT("{id}")
    suspend fun updPlaylist(@Path("id") id: Long, @Body playlist: Playlist): Response<Playlist>

    @DELETE("{id}")
    suspend fun delPlaylist(@Path("id") id: Long): Response<Unit>

    // add and rm musics, same for album
    @POST("{id}/musics/{musicId}")
    suspend fun addMusicToPlaylist(@Path("id") idPlaylist: Long, @Path("musicId") idMusic: Long): Response<Playlist>

    @DELETE("{id}/musics/{musicId}")
    suspend fun delMusicFromPlaylist(@Path("id") idPlaylist: Long, @Path("musicId") idMusic: Long): Response<Playlist>
}

object PlaylistClient{
    private const val BASE_URL = "https://resonance-api-bfax.onrender.com/playlists/"

    val apiService: PlaylistAPIService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaylistAPIService::class.java)
    }
}