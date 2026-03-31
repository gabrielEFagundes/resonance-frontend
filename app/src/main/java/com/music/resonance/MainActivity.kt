package com.music.resonance




import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.resonance.data.AlbumSummaryDto
import com.music.resonance.data.ArtistResponseDto
import com.music.resonance.data.MusicDto
import com.music.resonance.data.PlaylistDto
import com.music.resonance.data.ResonanceApi
import com.music.resonance.data.UserDto
import com.music.resonance.ui.screens.AlbumScreen
import com.music.resonance.ui.screens.AlbumTrackUi
import com.music.resonance.ui.screens.AlbumDetailUi
import com.music.resonance.ui.screens.ArtistScreen
import com.music.resonance.ui.screens.ArtistAlbumTileUi
import com.music.resonance.ui.screens.ArtistProfileUi
import com.music.resonance.ui.screens.MusicScreen
import com.music.resonance.ui.screens.MusicSectionUi
import com.music.resonance.ui.screens.MusicTrackUi
import com.music.resonance.ui.screens.PlaylistScreen
import com.music.resonance.ui.screens.PlaylistUi
import com.music.resonance.ui.screens.UserScreen
import com.music.resonance.ui.screens.sampleAlbumDetailById
import com.music.resonance.ui.screens.sampleArtistProfileById
import com.music.resonance.ui.theme.ResonanceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import coil.compose.AsyncImage
import kotlin.math.absoluteValue




private sealed class ResonanceRoute {
    data object Library : ResonanceRoute()
    data object User : ResonanceRoute()
    data object Music : ResonanceRoute()
    data object Playlist : ResonanceRoute()
    data object CreatePlaylist : ResonanceRoute()
    data object CreateAlbum : ResonanceRoute()
    data class Artist(val artistId: String) : ResonanceRoute()
    data class Album(val albumId: String, val popToArtistId: String?) : ResonanceRoute()
}


private data class ApiBootstrap(
    val artists: List<ArtistResponseDto>,
    val albums: List<AlbumSummaryDto>,
    val musics: List<MusicDto>,
    val playlists: List<PlaylistDto>,
    val users: List<UserDto>
)


private data class LibraryRemoteState(
    val albumSections: List<AlbumSection>,
    val artistSections: List<AlbumSection>,
    val artistProfilesById: Map<String, ArtistProfileUi>,
    val albumDetailsById: Map<String, AlbumDetailUi>,
    val musicSections: List<MusicSectionUi>,
    val musicOptions: List<Pair<Long, String>>,
    val playlists: List<PlaylistUi>,
    val userName: String,
    val userId: Long?,
    val registrationLabel: String,
    val profileImageUrl: String?,
    val postedPlaylists: List<String>,
    val postedAlbums: List<String>
)




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResonanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().background(color = Color(0xFF1B1D22)).safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var remoteState by remember { mutableStateOf<LibraryRemoteState?>(null) }

                    LaunchedEffect(Unit) {
                        remoteState = withContext(Dispatchers.IO) {
                            runCatching { loadLibraryRemoteState() }.getOrElse {
                                LibraryRemoteState(
                                    albumSections = sampleAlbumSections(),
                                    artistSections = sampleArtistSections(),
                                    artistProfilesById = emptyMap(),
                                    albumDetailsById = emptyMap(),
                                    musicSections = sampleMusicSections(),
                                    musicOptions = emptyList(),
                                    playlists = emptyList(),
                                    userName = "Usuario",
                                    userId = null,
                                    registrationLabel = "Inscricao indisponivel",
                                    profileImageUrl = null,
                                    postedPlaylists = emptyList(),
                                    postedAlbums = emptyList()
                                )
                            }
                        }
                    }

                    if (remoteState == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF23A7A2))
                        }
                        return@Surface
                    }

                    val scope = rememberCoroutineScope()
                    val state = remoteState!!
                    var localPlaylists by remember(state.playlists) { mutableStateOf(state.playlists) }
                    var localAlbumDetailsById by remember(state.albumDetailsById) { mutableStateOf(state.albumDetailsById) }
                    var localPostedPlaylists by remember(state.postedPlaylists) { mutableStateOf(state.postedPlaylists) }
                    var localPostedAlbums by remember(state.postedAlbums) { mutableStateOf(state.postedAlbums) }
                    var localProfileImageUrl by remember(state.profileImageUrl) { mutableStateOf(state.profileImageUrl) }
                    var actionMessage by remember { mutableStateOf<String?>(null) }
                    var route by remember { mutableStateOf<ResonanceRoute>(ResonanceRoute.Library) }
                    var librarySelectedFilter by remember { mutableStateOf("Álbuns") }
                    BackHandler(enabled = route != ResonanceRoute.Library) {
                        route = when (val r = route) {
                            ResonanceRoute.Library -> ResonanceRoute.Library
                            ResonanceRoute.User -> ResonanceRoute.Library
                            ResonanceRoute.Music -> ResonanceRoute.Library
                            ResonanceRoute.Playlist -> ResonanceRoute.User
                            ResonanceRoute.CreatePlaylist -> ResonanceRoute.User
                            ResonanceRoute.CreateAlbum -> ResonanceRoute.User
                            is ResonanceRoute.Artist -> ResonanceRoute.Library
                            is ResonanceRoute.Album -> r.popToArtistId?.let { ResonanceRoute.Artist(it) }
                                ?: ResonanceRoute.Library
                        }
                    }
                    when (val r = route) {
                        ResonanceRoute.Library -> MusicLibraryScreen(
                            albumSections = state.albumSections,
                            artistSections = state.artistSections,
                            profileImageUrl = localProfileImageUrl,
                            selectedFilter = librarySelectedFilter,
                            onSelectedFilterChange = { librarySelectedFilter = it },
                            onAlbumOpen = { albumId ->
                                if (state.albumDetailsById[albumId] != null || sampleAlbumDetailById(albumId) != null) {
                                    route = ResonanceRoute.Album(albumId, popToArtistId = null)
                                }
                            },
                            onArtistOpen = { artistId ->
                                if (state.artistProfilesById[artistId] != null || sampleArtistProfileById(artistId) != null) {
                                    route = ResonanceRoute.Artist(artistId)
                                }
                            },
                            onUserIconClick = {
                                route = ResonanceRoute.User
                            },
                            onMusicOpen = {
                                route = ResonanceRoute.Music
                            }
                        )
                        ResonanceRoute.User -> UserScreen(
                            onBack = { route = ResonanceRoute.Library },
                            onOpenPlaylists = { route = ResonanceRoute.CreatePlaylist },
                            onOpenAlbums = { route = ResonanceRoute.CreateAlbum },
                            remoteProfileImageUrl = localProfileImageUrl,
                            onProfileImageUriChanged = { imageUrl ->
                                localProfileImageUrl = imageUrl
                                val userId = state.userId
                                if (userId == null) {
                                    actionMessage = "Usuario nao encontrado para salvar foto."
                                } else {
                                    scope.launch {
                                        val saved = updateProfileImage(userId, state.userName, state.registrationLabel, imageUrl)
                                        actionMessage = if (saved) "Foto de perfil salva na API." else "Nao foi possivel salvar foto de perfil."
                                    }
                                }
                            },
                            userName = state.userName,
                            registrationLabel = state.registrationLabel,
                            postedPlaylists = localPostedPlaylists,
                            postedAlbums = localPostedAlbums
                        )
                        ResonanceRoute.Music -> MusicScreen(
                            onBack = { route = ResonanceRoute.Library },
                            onUserIconClick = { route = ResonanceRoute.User },
                            onAlbumsClick = {
                                librarySelectedFilter = "Álbuns"
                                route = ResonanceRoute.Library
                            },
                            onArtistsClick = {
                                librarySelectedFilter = "Artistas"
                                route = ResonanceRoute.Library
                            },
                            sections = state.musicSections,
                            profileImageUrl = localProfileImageUrl
                        )
                        ResonanceRoute.Playlist -> PlaylistScreen(
                            playlists = localPlaylists,
                            onBack = { route = ResonanceRoute.User },
                            defaultArtistId = state.artistProfilesById.keys.firstOrNull()?.toLongOrNull() ?: 1L,
                            onUpdatePlaylist = { id, title ->
                                val updated = updatePlaylist(id, title, state.userId ?: 1L)
                                if (updated != null) {
                                    localPlaylists = localPlaylists.map { if (it.id == id) updated else it }
                                    true
                                } else false
                            },
                            onDeletePlaylist = { id ->
                                val removedTitle = localPlaylists.find { it.id == id }?.title
                                val ok = deletePlaylist(id)
                                if (ok) {
                                    localPlaylists = localPlaylists.filterNot { it.id == id }
                                    if (removedTitle != null) {
                                        localPostedPlaylists = localPostedPlaylists.filterNot { it == removedTitle }
                                    }
                                }
                                ok
                            },
                            onAddMusicToPlaylist = { playlistId, title, durationSec, genre ->
                                val artistId = state.artistProfilesById.keys.firstOrNull()?.toLongOrNull() ?: 1L
                                val ok = addMusicToPlaylistApi(playlistId, title, durationSec, genre, artistId)
                                if (ok) {
                                    localPlaylists = localPlaylists.map { p ->
                                        if (p.id == playlistId) p.copy(trackCount = p.trackCount + 1) else p
                                    }
                                }
                                ok
                            }
                        )
                        ResonanceRoute.CreatePlaylist -> PlaylistScreen(
                            playlists = localPlaylists,
                            onBack = { route = ResonanceRoute.User },
                            isCreateMode = true,
                            createMessage = actionMessage,
                            musicOptions = state.musicOptions,
                            onCreatePlaylist = { title, musicIds ->
                                val userId = state.userId ?: 1L
                                val created = createPlaylist(title, userId, musicIds)
                                if (created != null) {
                                    localPlaylists = localPlaylists + created
                                    localPostedPlaylists = localPostedPlaylists + created.title
                                    actionMessage = "Playlist criada com sucesso."
                                    true
                                } else {
                                    actionMessage = "Falha ao criar playlist na API."
                                    false
                                }
                            }
                        )
                        ResonanceRoute.CreateAlbum -> AlbumScreen(
                            detail = null,
                            onBack = { route = ResonanceRoute.User },
                            isCreateMode = true,
                            createMessage = actionMessage,
                            musicOptions = state.musicOptions,
                            onCreateAlbum = { title, releaseYear, musicIds ->
                                val artistId = state.artistProfilesById.keys.firstOrNull()?.toLongOrNull() ?: 1L
                                val created = createAlbum(title, releaseYear, artistId, musicIds)
                                if (created != null) {
                                    localAlbumDetailsById = localAlbumDetailsById + (created.id to created)
                                    localPostedAlbums = localPostedAlbums + created.title
                                    actionMessage = "Album criado com sucesso."
                                    true
                                } else {
                                    actionMessage = "Falha ao criar album na API."
                                    false
                                }
                            }
                        )
                        is ResonanceRoute.Artist -> {
                            val profile = state.artistProfilesById[r.artistId]
                                ?: sampleArtistProfileById(r.artistId)
                            if (profile != null) {
                                ArtistScreen(
                                    profile = profile,
                                    onBack = { route = ResonanceRoute.Library },
                                    onAlbumOpen = { albumId ->
                                        if (state.albumDetailsById[albumId] != null || sampleAlbumDetailById(albumId) != null) {
                                            route = ResonanceRoute.Album(
                                                albumId,
                                                popToArtistId = r.artistId
                                            )
                                        }
                                    }
                                )
                            } else {
                                route = ResonanceRoute.Library
                            }
                        }
                        is ResonanceRoute.Album -> {
                            val detail = localAlbumDetailsById[r.albumId]
                                ?: sampleAlbumDetailById(r.albumId)
                            if (detail != null) {
                                AlbumScreen(
                                    detail = detail,
                                    onUpdateAlbum = { id, title, releaseYear ->
                                        val artistId = state.artistProfilesById.keys.firstOrNull()?.toLongOrNull() ?: 1L
                                        val updated = updateAlbum(id, title, releaseYear, artistId)
                                        if (updated != null) {
                                            localAlbumDetailsById = localAlbumDetailsById + (id to updated)
                                            true
                                        } else false
                                    },
                                    onDeleteAlbum = { id ->
                                        val ok = deleteAlbum(id)
                                        if (ok) {
                                            localAlbumDetailsById = localAlbumDetailsById - id
                                        }
                                        ok
                                    },
                                    onAddMusicToAlbum = { title, durationSec, genre ->
                                        val artistId = state.artistProfilesById.keys.firstOrNull()?.toLongOrNull() ?: 1L
                                        val updated = addMusicToAlbumApi(r.albumId, title, durationSec, genre, artistId)
                                        if (updated != null) {
                                            localAlbumDetailsById = localAlbumDetailsById + (r.albumId to updated)
                                            true
                                        } else false
                                    },
                                    onBack = {
                                        route = r.popToArtistId?.let { ResonanceRoute.Artist(it) }
                                            ?: ResonanceRoute.Library
                                    }
                                )
                            } else {
                                route = ResonanceRoute.Library
                            }
                        }
                    }
                }
            }
        }
    }
}




@Composable
private fun MusicLibraryScreen(
    albumSections: List<AlbumSection>,
    artistSections: List<AlbumSection>,
    profileImageUrl: String?,
    selectedFilter: String,
    onSelectedFilterChange: (String) -> Unit,
    onAlbumOpen: (String) -> Unit,
    onArtistOpen: (String) -> Unit,
    onUserIconClick: () -> Unit,
    onMusicOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentSections = if (selectedFilter == "Álbuns") albumSections else artistSections




    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE6E6E6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1B1D22))
                .padding(top = 20.dp)
        ) {
            HeaderSection(onUserIconClick = onUserIconClick, profileImageUrl = profileImageUrl)
            Spacer(modifier = Modifier.height(14.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(14.dp))
            FilterToggle(
                selected = selectedFilter,
                onSelected = onSelectedFilterChange,
                onMusicOpen = onMusicOpen
            )
            Spacer(modifier = Modifier.height(10.dp))
            LibrarySections(
                sections = currentSections,
                onAlbumOpen = if (selectedFilter == "Álbuns") onAlbumOpen else null,
                onArtistOpen = if (selectedFilter == "Artistas") onArtistOpen else null
            )
        }
    }
}


/**
 * Plano gratuito do Render “dorme” o serviço. Várias requisições paralelas no primeiro ping costumam
 * falhar todas ao mesmo tempo. Uma sequência leve com backoff dá tempo do backend ficar pronto.
 */
private suspend fun wakeBackendAfterColdStart() {
    repeat(5) { attempt ->
        val ok = runCatching { ResonanceApi.service.getArtists() }.isSuccess
        if (ok) return
        if (attempt < 4) delay(2_500L + attempt * 1_500L)
    }
}

private suspend fun fetchBootstrap(endpointErrors: MutableList<String>): ApiBootstrap = coroutineScope {
    val dArtists = async { runCatching { ResonanceApi.service.getArtists() } }
    val dAlbums = async { runCatching { ResonanceApi.service.getAlbums() } }
    val dMusics = async { runCatching { ResonanceApi.service.getMusics() } }
    val dPlaylists = async { runCatching { ResonanceApi.service.getPlaylists() } }
    val dUsers = async { runCatching { ResonanceApi.service.getUsers() } }
    ApiBootstrap(
        artists = dArtists.await().getOrElse { endpointErrors += "/artists"; emptyList() },
        albums = dAlbums.await().getOrElse { endpointErrors += "/albums"; emptyList() },
        musics = dMusics.await().getOrElse { endpointErrors += "/musics"; emptyList() },
        playlists = dPlaylists.await().getOrElse { endpointErrors += "/playlists"; emptyList() },
        users = dUsers.await().getOrElse { endpointErrors += "/users"; emptyList() }
    )
}

private suspend fun loadLibraryRemoteState(): LibraryRemoteState {
    val endpointErrors = mutableListOf<String>()
    var bootstrap = fetchBootstrap(endpointErrors)
    if (endpointErrors.size >= 5) {
        endpointErrors.clear()
        wakeBackendAfterColdStart()
        bootstrap = fetchBootstrap(endpointErrors)
    }
    val artists = bootstrap.artists
    val albums = bootstrap.albums
    val musics = bootstrap.musics
    val playlists = bootstrap.playlists
    val users = bootstrap.users


    val artistsById = artists.mapNotNull { artist ->
        artist.id?.let { it to artist }
    }.toMap()
    val albumDetails = coroutineScope {
        albums.mapNotNull { summary ->
            val albumId = summary.id ?: return@mapNotNull null
            async {
                runCatching {
                    val detail = ResonanceApi.service.getAlbumById(albumId)
                    val detailId = detail.id ?: albumId
                    detailId.toString() to detail.toAlbumDetailUi(artistsById[detail.artistId])
                }.getOrElse {
                    endpointErrors += "/albums/$albumId"
                    albumId.toString() to AlbumDetailUi(
                        id = albumId.toString(),
                        title = summary.title ?: "Album sem titulo",
                        artist = artistsById[summary.artistId]?.displayName() ?: "Artista #${summary.artistId ?: "?"}",
                        year = summary.releaseYear ?: 0,
                        coverGradientStart = colorLongA(albumId.toString()),
                        coverGradientEnd = colorLongB(albumId.toString()),
                        tracks = emptyList()
                    )
                }
            }
        }.awaitAll().filterNotNull().toMap()
    }


    val albumSections = listOf(
        AlbumSection(
            title = "API - Álbuns",
            albums = albums.mapNotNull { album ->
                if (album.id == null) null else album.toAlbumItem(artistsById[album.artistId])
            }
        )
    )


    val artistSections = listOf(
        AlbumSection(
            title = "API - Artistas",
            albums = artists.mapNotNull { artist ->
                val artistId = artist.id ?: return@mapNotNull null
                AlbumItem(
                    id = artistId.toString(),
                    name = artist.displayName(),
                    artist = artist.monthlyListeners?.let { "$it ouvintes mensais" }
                        ?: "Artista",
                    tag = artist.displayName().split(" ").mapNotNull { it.firstOrNull()?.toString() }
                        .take(2).joinToString(""),
                    colors = colorsForSeed(artistId.toString())
                )
            }
        )
    )


    val artistProfiles = artists.mapNotNull artistLoop@ { artist ->
        val artistId = artist.id ?: return@artistLoop null
        val relatedAlbums = albums.filter { it.artistId == artistId }
        artistId.toString() to ArtistProfileUi(
            id = artistId.toString(),
            name = artist.displayName(),
            listenersLine = artist.monthlyListeners?.let { "$it ouvintes mensais" }
                ?: "Sem ouvintes mensais",
            bioIntro = artist.description ?: "Sem descricao",
            bioExtra = "",
            heroGradientStart = colorLongA(artistId.toString()),
            heroGradientEnd = colorLongB(artistId.toString()),
            albums = relatedAlbums.mapNotNull albumLoop@ { album ->
                val albumId = album.id ?: return@albumLoop null
                ArtistAlbumTileUi(
                    albumId = albumId.toString(),
                    displayTitle = album.title ?: "Album sem titulo",
                    coverGradientStart = colorLongA(albumId.toString()),
                    coverGradientEnd = colorLongB(albumId.toString())
                )
            }
        )
    }.toMap()


    val musicsByGenre = musics.groupBy { it.genre?.ifBlank { null } ?: "Outros" }
    val musicSections = musicsByGenre.entries.map { (genre, list) ->
        MusicSectionUi(
            title = genre,
            tracks = list.mapIndexed { idx, music ->
                MusicTrackUi(
                    number = idx + 1,
                    title = music.title ?: "Faixa sem titulo",
                    duration = formatDuration(music.duration)
                )
            }
        )
    }.ifEmpty { sampleMusicSections() }
    val musicOptions = musics.mapNotNull { music ->
        music.id?.let { it to (music.title ?: "Musica #$it") }
    }


    val usersById = users.mapNotNull { user ->
        user.id?.let { it to user }
    }.toMap()
    val playlistUis = playlists.mapNotNull { playlist ->
        val playlistId = playlist.id ?: return@mapNotNull null
        PlaylistUi(
            id = playlistId.toString(),
            title = playlist.title ?: "Playlist sem titulo",
            trackCount = playlist.musics?.size ?: 0,
            owner = usersById[playlist.userId]?.name ?: "Usuario #${playlist.userId ?: "?"}"
        )
    }


    val firstUser = users.firstOrNull()
    val userName = firstUser?.name ?: "Usuario"
    val userId = firstUser?.id
    val registrationLabel = "Login: ${firstUser?.loginDate ?: "N/A"}"
    val profileImageUrl = firstUser?.profilePictureUrl
    val postedPlaylists = playlistUis.filter { p ->
        userId == null || usersById[userId]?.name == p.owner
    }.map { it.title }.ifEmpty { playlistUis.map { it.title } }
    val postedAlbums = albums.take(8).map { it.title ?: "Album sem titulo" }


    return LibraryRemoteState(
        albumSections = albumSections,
        artistSections = artistSections,
        artistProfilesById = artistProfiles,
        albumDetailsById = albumDetails,
        musicSections = musicSections,
        musicOptions = musicOptions,
        playlists = playlistUis,
        userName = userName,
        userId = userId,
        registrationLabel = registrationLabel,
        profileImageUrl = profileImageUrl,
        postedPlaylists = postedPlaylists,
        postedAlbums = postedAlbums
    )
}

private suspend fun createPlaylist(title: String, userId: Long, musicIds: List<Long>): PlaylistUi? {
    return runCatching {
        var response = ResonanceApi.service.createPlaylist(
            com.music.resonance.data.CreatePlaylistRequestDto(
                title = title,
                userId = userId
            )
        )
        val playlistId = response.id ?: return@runCatching null
        musicIds.forEach { musicId ->
            response = ResonanceApi.service.addMusicToPlaylist(playlistId, musicId)
        }
        PlaylistUi(
            id = playlistId.toString(),
            title = response.title ?: title,
            trackCount = response.musics?.size ?: 0,
            owner = "Usuario #$userId"
        )
    }.getOrNull()
}

private suspend fun updatePlaylist(id: String, title: String, userId: Long): PlaylistUi? {
    val playlistId = id.toLongOrNull() ?: return null
    return runCatching {
        val response = ResonanceApi.service.updatePlaylist(
            playlistId,
            com.music.resonance.data.UpdatePlaylistRequestDto(
                id = playlistId,
                title = title,
                userId = userId
            )
        )
        PlaylistUi(
            id = playlistId.toString(),
            title = response.title ?: title,
            trackCount = response.musics?.size ?: 0,
            owner = "Usuario #$userId"
        )
    }.getOrNull()
}

private suspend fun deletePlaylist(id: String): Boolean {
    val playlistId = id.toLongOrNull() ?: return false
    return runCatching { ResonanceApi.service.deletePlaylist(playlistId).isSuccessful }.getOrDefault(false)
}

private suspend fun createMusicApi(
    title: String,
    durationSec: Int,
    genre: String,
    artistId: Long
): Long? {
    return runCatching {
        val m = ResonanceApi.service.createMusic(
            com.music.resonance.data.CreateMusicRequestDto(
                title = title,
                artistId = artistId,
                duration = durationSec,
                genre = genre.ifBlank { "POP" }
            )
        )
        m.id
    }.getOrNull()
}

private suspend fun addMusicToPlaylistApi(
    playlistId: String,
    title: String,
    durationSec: Int,
    genre: String,
    artistId: Long
): Boolean {
    val pid = playlistId.toLongOrNull() ?: return false
    val musicId = createMusicApi(title, durationSec, genre, artistId) ?: return false
    return runCatching { ResonanceApi.service.addMusicToPlaylist(pid, musicId) }.isSuccess
}

private suspend fun addMusicToAlbumApi(
    albumId: String,
    title: String,
    durationSec: Int,
    genre: String,
    artistId: Long
): AlbumDetailUi? {
    val aid = albumId.toLongOrNull() ?: return null
    val musicId = createMusicApi(title, durationSec, genre, artistId) ?: return null
    return runCatching {
        val detail = ResonanceApi.service.addMusicToAlbum(aid, musicId)
        detail.toAlbumDetailUi(null)
    }.getOrNull()
}

private suspend fun createAlbum(title: String, releaseYear: Int, artistId: Long, musicIds: List<Long>): AlbumDetailUi? {
    return runCatching {
        var response = ResonanceApi.service.createAlbum(
            com.music.resonance.data.CreateAlbumRequestDto(
                title = title,
                releaseYear = releaseYear,
                artistId = artistId
            )
        )
        val albumIdLong = response.id ?: return@runCatching null
        var tracks = emptyList<AlbumTrackUi>()
        musicIds.forEach { musicId ->
            val detail = ResonanceApi.service.addMusicToAlbum(albumIdLong, musicId)
            tracks = (detail.musics ?: emptyList()).mapIndexed { idx, music ->
                AlbumTrackUi(idx + 1, music.title ?: "Faixa ${idx + 1}", formatDuration(music.duration))
            }
            response = com.music.resonance.data.AlbumSummaryDto(
                id = detail.id,
                title = detail.title,
                releaseYear = detail.releaseYear,
                artistId = detail.artistId
            )
        }
        val albumId = albumIdLong.toString()
        AlbumDetailUi(
            id = albumId,
            title = response.title ?: title,
            artist = "Artista #$artistId",
            year = response.releaseYear ?: releaseYear,
            coverGradientStart = colorLongA(albumId),
            coverGradientEnd = colorLongB(albumId),
            tracks = tracks
        )
    }.getOrNull()
}

private suspend fun updateAlbum(id: String, title: String, releaseYear: Int, artistId: Long): AlbumDetailUi? {
    val albumId = id.toLongOrNull() ?: return null
    return runCatching {
        val response = ResonanceApi.service.updateAlbum(
            albumId,
            com.music.resonance.data.UpdateAlbumRequestDto(
                id = albumId,
                title = title,
                releaseYear = releaseYear,
                artistId = artistId
            )
        )
        AlbumDetailUi(
            id = albumId.toString(),
            title = response.title ?: title,
            artist = "Artista #$artistId",
            year = response.releaseYear ?: releaseYear,
            coverGradientStart = colorLongA(albumId.toString()),
            coverGradientEnd = colorLongB(albumId.toString()),
            tracks = emptyList()
        )
    }.getOrNull()
}

private suspend fun deleteAlbum(id: String): Boolean {
    val albumId = id.toLongOrNull() ?: return false
    return runCatching { ResonanceApi.service.deleteAlbum(albumId).isSuccessful }.getOrDefault(false)
}

private suspend fun updateProfileImage(
    userId: Long,
    userName: String,
    registrationLabel: String,
    profilePictureUrl: String
): Boolean {
    val loginDate = registrationLabel.removePrefix("Login: ").ifBlank { "N/A" }
    return runCatching {
        ResonanceApi.service.updateUser(
            userId,
            com.music.resonance.data.UpdateUserRequestDto(
                id = userId,
                name = userName,
                loginDate = loginDate,
                profilePictureUrl = profilePictureUrl
            )
        )
    }.isSuccess
}


private fun com.music.resonance.data.ArtistResponseDto.displayName(): String {
    return artisticName?.takeIf { it.isNotBlank() }
        ?: name?.takeIf { it.isNotBlank() }
        ?: "Artista #${id ?: "?"}"
}


private fun com.music.resonance.data.AlbumSummaryDto.toAlbumItem(
    artist: com.music.resonance.data.ArtistResponseDto?
): AlbumItem {
    val albumId = id ?: 0L
    return AlbumItem(
        id = albumId.toString(),
        name = title ?: "Album sem titulo",
        artist = artist?.displayName() ?: "Artista #${artistId ?: "?"}",
        tag = (title ?: "AL").take(2).uppercase(),
        colors = colorsForSeed(albumId.toString())
    )
}


private fun com.music.resonance.data.AlbumDetailDto.toAlbumDetailUi(
    artist: com.music.resonance.data.ArtistResponseDto?
): AlbumDetailUi {
    val tracks = (musics ?: emptyList()).mapIndexed { idx, music ->
        AlbumTrackUi(
            number = idx + 1,
            title = music.title ?: "Faixa ${idx + 1}",
            duration = formatDuration(music.duration)
        )
    }


    val albumId = id ?: 0L
    return AlbumDetailUi(
        id = albumId.toString(),
        title = title ?: "Album sem titulo",
        artist = artist?.displayName() ?: "Artista #${artistId ?: "?"}",
        year = releaseYear ?: 0,
        coverGradientStart = colorLongA(albumId.toString()),
        coverGradientEnd = colorLongB(albumId.toString()),
        tracks = tracks
    )
}


private fun formatDuration(durationSeconds: Int?): String {
    val total = durationSeconds ?: return "--:--"
    val min = total / 60
    val sec = total % 60
    return "%d:%02d".format(min, sec)
}


private fun sampleMusicSections(): List<MusicSectionUi> {
    return listOf(
        MusicSectionUi(
            title = "Popular",
            tracks = listOf(
                MusicTrackUi(1, "NUEVAYol", "3:03"),
                MusicTrackUi(2, "VOY A LLeVARTE PA PR", "2:36"),
                MusicTrackUi(3, "BAILE INoLVIDABLE", "2:36")
            )
        )
    )
}


private fun colorsForSeed(seed: String): List<Color> {
    return listOf(Color(colorLongA(seed)), Color(colorLongB(seed)))
}


private fun colorLongA(seed: String): Long {
    val hash = seed.hashCode().absoluteValue
    val r = 60 + (hash % 120)
    val g = 50 + ((hash / 7) % 120)
    val b = 70 + ((hash / 11) % 120)
    return (0xFF000000L or (r.toLong() shl 16) or (g.toLong() shl 8) or b.toLong())
}


private fun colorLongB(seed: String): Long {
    val hash = (seed.hashCode() * 31).absoluteValue
    val r = 120 + (hash % 100)
    val g = 90 + ((hash / 5) % 100)
    val b = 110 + ((hash / 9) % 100)
    return (0xFF000000L or (r.toLong() shl 16) or (g.toLong() shl 8) or b.toLong())
}




@Composable
private fun HeaderSection(onUserIconClick: () -> Unit, profileImageUrl: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Explore",
                color = Color(0xFFC8D0D0),
                fontSize = 15.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1AA5A2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "◀",
                        color = Color.White,
                        fontSize = 9.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Resonance",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF3A3D44))
                .clickable { onUserIconClick() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUrl.isNullOrBlank()) {
                Text(
                    text = "ED",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            } else {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}




@Composable
private fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1E6768))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Buscar",
            tint = Color(0xFFB9D7D8)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Encontre albuns/artistas",
            color = Color(0xFFB9D7D8),
            fontSize = 12.sp
        )
    }
}




@Composable
private fun FilterToggle(
    selected: String,
    onSelected: (String) -> Unit,
    onMusicOpen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        FilterPill(
            title = "Álbuns",
            isSelected = selected == "Álbuns",
            onClick = { onSelected("Álbuns") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        FilterPill(
            title = "Artistas",
            isSelected = selected == "Artistas",
            onClick = { onSelected("Artistas") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        FilterPill(
            title = "Músicas",
            isSelected = false,
            onClick = onMusicOpen
        )
    }
}




@Composable
private fun FilterPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected) Color(0xFF23A7A2) else Color.Transparent
    val textColor = if (isSelected) Color(0xFFDEFFFF) else Color(0xFFE5E5E5)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(background)
            .clickable { onClick() }
            .padding(horizontal = 22.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}




@Composable
private fun LibrarySections(
    sections: List<AlbumSection>,
    onAlbumOpen: ((String) -> Unit)?,
    onArtistOpen: ((String) -> Unit)?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sections) { section ->
            Column {
                Text(
                    text = section.title,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp)
                ) {
                    items(section.albums) { album ->
                        val openHandler = onAlbumOpen?.let { open -> { open(album.id) } }
                            ?: onArtistOpen?.let { open -> { open(album.id) } }
                        AlbumCard(
                            album = album,
                            onClick = openHandler
                        )
                    }
                }
            }
        }
    }
}




@Composable
private fun AlbumCard(
    album: AlbumItem,
    onClick: (() -> Unit)?
) {
    val columnModifier = Modifier
        .width(140.dp)
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    Column(modifier = columnModifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.linearGradient(
                        colors = album.colors
                    )
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = album.tag,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = album.name,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
        Text(
            text = album.artist,
            color = Color(0xFF9BA1AB),
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}




private data class AlbumSection(
    val title: String,
    val albums: List<AlbumItem>
)




private data class AlbumItem(
    val id: String,
    val name: String,
    val artist: String,
    val tag: String,
    val colors: List<Color>
)




private fun sampleAlbumSections(): List<AlbumSection> {
    return listOf(
        AlbumSection(
            title = "Popular",
            albums = listOf(
                AlbumItem(
                    id = "debi-tirar-mas-fotos",
                    name = "DeBi TIRAR MÁS FOTOs",
                    artist = "Bad Bunny",
                    tag = "BAD",
                    colors = listOf(Color(0xFF285439), Color(0xFF8BAE84))
                ),
                AlbumItem(
                    id = "significant-other",
                    name = "Significant Other",
                    artist = "Limp Bizkit",
                    tag = "LB",
                    colors = listOf(Color(0xFF54317A), Color(0xFFE0715D))
                ),
                AlbumItem(
                    id = "random-access-memories",
                    name = "Random Access Memories",
                    artist = "Daft Punk",
                    tag = "DP",
                    colors = listOf(Color(0xFF222634), Color(0xFFB88E2F))
                )
            )
        ),
        AlbumSection(
            title = "Rock & Roll",
            albums = listOf(
                AlbumItem(
                    id = "dr-feelgood",
                    name = "Dr. Feelgood",
                    artist = "Motley Crue",
                    tag = "MC",
                    colors = listOf(Color(0xFF446D63), Color(0xFFC95E57))
                ),
                AlbumItem(
                    id = "paranoid",
                    name = "Paranoid",
                    artist = "Black Sabbath",
                    tag = "BS",
                    colors = listOf(Color(0xFF19171D), Color(0xFFA53C27))
                ),
                AlbumItem(
                    id = "back-in-black",
                    name = "Back in Black",
                    artist = "AC/DC",
                    tag = "AC",
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF6E6E6E))
                )
            )
        ),
        AlbumSection(
            title = "Hip Hop",
            albums = listOf(
                AlbumItem(
                    id = "damn",
                    name = "DAMN.",
                    artist = "Kendrick Lamar",
                    tag = "KL",
                    colors = listOf(Color(0xFF743B3B), Color(0xFFF2D2BF))
                ),
                AlbumItem(
                    id = "astroworld",
                    name = "Astroworld",
                    artist = "Travis Scott",
                    tag = "TS",
                    colors = listOf(Color(0xFF8B5A2B), Color(0xFF21215B))
                ),
                AlbumItem(
                    id = "eminem-show",
                    name = "The Eminem Show",
                    artist = "Eminem",
                    tag = "EM",
                    colors = listOf(Color(0xFF1E6A73), Color(0xFFBBE6E8))
                )
            )
        ))
}








private fun sampleArtistSections(): List<AlbumSection> {
    return listOf(
        AlbumSection(
            title = "Popular",
            albums = listOf(
                AlbumItem(
                    id = "artist-bad-bunny",
                    name = "Bad Bunny",
                    artist = "74,4.775.730 ouvintes mensais",
                    tag = "BB",
                    colors = listOf(Color(0xFF2DBB4D), Color(0xFF064B12))
                ),
                AlbumItem(
                    id = "artist-sabrina",
                    name = "Sabrina Carpenter",
                    artist = "84.101.805 ouvintes mensais",
                    tag = "SC",
                    colors = listOf(Color(0xFFD39AA0), Color(0xFF7F4C53))
                ),
                AlbumItem(
                    id = "artist-weeknd",
                    name = "The Weeknd",
                    artist = "111.642.112 ouvintes mensais",
                    tag = "TW",
                    colors = listOf(Color(0xFF121212), Color(0xFF4D4D4D))
                )
            )
        ),
        AlbumSection(
            title = "Velha Guarda",
            albums = listOf(
                AlbumItem(
                    id = "artist-mj",
                    name = "Michael Jackson",
                    artist = "62.136.109 ouvintes mensais",
                    tag = "MJ",
                    colors = listOf(Color(0xFFF0F0F0), Color(0xFF8C8C8C))
                ),
                AlbumItem(
                    id = "artist-seu-jorge",
                    name = "Seu Jorge",
                    artist = "6.186.869 ouvintes mensais",
                    tag = "SJ",
                    colors = listOf(Color(0xFF2B2B2B), Color(0xFF6E4F42))
                ),
                AlbumItem(
                    id = "artist-tim-maia",
                    name = "Tim Maia",
                    artist = "5.220.441 ouvintes mensais",
                    tag = "TM",
                    colors = listOf(Color(0xFF513722), Color(0xFFD29462))
                )
            )
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MusicLibraryPreview() {
    ResonanceTheme {
        MusicLibraryScreen(
            albumSections = sampleAlbumSections(),
            artistSections = sampleArtistSections(),
            profileImageUrl = null,
            selectedFilter = "Álbuns",
            onSelectedFilterChange = {},
            onAlbumOpen = {},
            onArtistOpen = {},
            onUserIconClick = {},
            onMusicOpen = {}
        )
    }
}
