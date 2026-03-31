package com.music.resonance.data


import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
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
    val genre: String?,
    @SerializedName(value = "coverImageUrl", alternate = ["cover_image_url"])
    val coverImageUrl: String? = null
)

data class CreateMusicRequestDto(
    val title: String,
    val artistId: Long,
    val duration: Int,
    val genre: String = "POP",
    val coverImageUrl: String = ""
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
    val loginDate: String?,
    @SerializedName(value = "profilePictureUrl", alternate = ["profile_picture_url"])
    val profilePictureUrl: String?
)

data class CreatePlaylistRequestDto(
    val title: String,
    val userId: Long,
    val coverImageUrl: String = ""
)

data class CreateAlbumRequestDto(
    val title: String,
    val releaseYear: Int,
    val artistId: Long,
    val coverImageUrl: String = ""
)

data class UpdatePlaylistRequestDto(
    val id: Long,
    val title: String,
    val userId: Long,
    val coverImageUrl: String = ""
)

data class UpdateAlbumRequestDto(
    val id: Long,
    val title: String,
    val releaseYear: Int,
    val artistId: Long,
    val coverImageUrl: String = ""
)

data class UpdateUserRequestDto(
    val id: Long,
    val name: String,
    val loginDate: String,
    val profilePictureUrl: String?
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

    @POST("musics")
    suspend fun createMusic(@Body body: CreateMusicRequestDto): MusicDto

    @POST("playlists")
    suspend fun createPlaylist(@Body body: CreatePlaylistRequestDto): PlaylistDto

    @PUT("playlists/{id}")
    suspend fun updatePlaylist(@Path("id") id: Long, @Body body: UpdatePlaylistRequestDto): PlaylistDto

    @DELETE("playlists/{id}")
    suspend fun deletePlaylist(@Path("id") id: Long): Response<ResponseBody>

    @POST("playlists/{id}/musics/{musicId}")
    suspend fun addMusicToPlaylist(@Path("id") id: Long, @Path("musicId") musicId: Long): PlaylistDto

    @POST("albums")
    suspend fun createAlbum(@Body body: CreateAlbumRequestDto): AlbumSummaryDto

    @PUT("albums/{id}")
    suspend fun updateAlbum(@Path("id") id: Long, @Body body: UpdateAlbumRequestDto): AlbumSummaryDto

    @DELETE("albums/{id}")
    suspend fun deleteAlbum(@Path("id") id: Long): Response<ResponseBody>

    @POST("albums/{id}/musics/{musicId}")
    suspend fun addMusicToAlbum(@Path("id") id: Long, @Path("musicId") musicId: Long): AlbumDetailDto

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body body: UpdateUserRequestDto): UserDto
}


object ResonanceApi {
    private const val BASE_URL = "https://resonance-api-bfax.onrender.com/"
    private val httpClient = OkHttpClient.Builder()
        // Cinco GETs em paralelo no cold start do Render: precisa de margem na leitura após o serviço acordar.
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
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
