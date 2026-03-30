package com.music.resonance.data


import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


data class ArtistResponseDto(
    val id: Long?,
    val name: String?,
    @SerializedName(value = "artisticName", alternate = ["artistic_name"])
    val artisticName: String?,
    @SerializedName(value = "monthlyListeners", alternate = ["monthly_listeners"])
    val monthlyListeners: Long?,
    val description: String?
)


data class AlbumSummaryDto(
    val id: Long?,
    val title: String?,
    @SerializedName(value = "releaseYear", alternate = ["release_year"])
    val releaseYear: Int?,
    val artistId: Long?
)


data class AlbumDetailDto(
    val id: Long?,
    val title: String?,
    @SerializedName(value = "releaseYear", alternate = ["release_year"])
    val releaseYear: Int?,
    val artistId: Long?,
    val musics: List<MusicDto>?
)


data class MusicDto(
    val id: Long?,
    val title: String?,
    @SerializedName("artistId")
    val artistId: Long?,
    val duration: Int?,
    val genre: String?
)


data class PlaylistDto(
    val id: Long?,
    val title: String?,
    val musics: List<MusicDto>?,
    val userId: Long?
)


data class UserDto(
    val id: Long?,
    val name: String?,
    @SerializedName(value = "loginDate", alternate = ["login_date"])
    val loginDate: String?
)


interface ResonanceApiService {
    @GET("artists")
    suspend fun getArtists(): List<ArtistResponseDto>


    @GET("albums")
    suspend fun getAlbums(): List<AlbumSummaryDto>


    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Long): AlbumDetailDto


    @GET("musics")
    suspend fun getMusics(): List<MusicDto>


    @GET("playlists")
    suspend fun getPlaylists(): List<PlaylistDto>


    @GET("users")
    suspend fun getUsers(): List<UserDto>
}


object ResonanceApi {
    private const val BASE_URL = "https://resonance-api-bfax.onrender.com/"
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()


    val service: ResonanceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ResonanceApiService::class.java)
    }
}
