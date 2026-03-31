package com.music.resonance.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.music.resonance.ui.theme.ResonanceTheme
import kotlinx.coroutines.launch


data class MusicTrackUi(
    val id: Long?,
    val artistId: Long?,
    val genre: String,
    val number: Int,
    val title: String,
    val duration: String
)


data class MusicSectionUi(
    val title: String,
    val tracks: List<MusicTrackUi>
)


@Composable
fun MusicScreen(
    onBack: () -> Unit,
    onUserIconClick: () -> Unit,
    onAlbumsClick: () -> Unit,
    onArtistsClick: () -> Unit,
    sections: List<MusicSectionUi>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    defaultArtistId: Long,
    onCreateMusic: suspend (title: String, durationSeconds: Int, genre: String, artistId: Long) -> Boolean,
    onDeleteMusic: suspend (musicId: Long) -> Boolean,
    onUpdateMusic: suspend (musicId: Long, title: String, durationSeconds: Int, genre: String, artistId: Long) -> Boolean,
    profileImageUrl: String? = null,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var newTitle by remember { mutableStateOf("") }
    var newDuration by remember { mutableStateOf("") }
    var newGenre by remember { mutableStateOf("POP") }
    var newArtistId by remember { mutableStateOf(defaultArtistId.toString()) }
    val filteredSections = filterMusicSectionsByQuery(sections, searchQuery)
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
            MusicHeader(
                onBack = onBack,
                onUserIconClick = onUserIconClick,
                profileImageUrl = profileImageUrl
            )
            Spacer(modifier = Modifier.height(14.dp))
            MusicSearchBar(query = searchQuery, onQueryChange = onSearchQueryChange)
            Spacer(modifier = Modifier.height(14.dp))
            MusicFilterRow(
                onAlbumsClick = onAlbumsClick,
                onArtistsClick = onArtistsClick
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF2C2F35))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Criar música", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = musicFieldColors()
                )
                OutlinedTextField(
                    value = newDuration,
                    onValueChange = { newDuration = it },
                    label = { Text("Duração (3:45 ou segundos)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = musicFieldColors()
                )
                OutlinedTextField(
                    value = newGenre,
                    onValueChange = { newGenre = it },
                    label = { Text("Gênero") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = musicFieldColors()
                )
                OutlinedTextField(
                    value = newArtistId,
                    onValueChange = { newArtistId = it.filter { ch -> ch.isDigit() } },
                    label = { Text("ID do artista") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = musicFieldColors()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1E6768))
                        .clickable(enabled = newTitle.isNotBlank()) {
                            val sec = parseMusicDurationToSeconds(newDuration) ?: 0
                            val artistId = newArtistId.toLongOrNull() ?: 0L
                            if (sec <= 0 || artistId <= 0L) return@clickable
                            scope.launch {
                                if (onCreateMusic(newTitle.trim(), sec, newGenre.trim(), artistId)) {
                                    newTitle = ""
                                    newDuration = ""
                                    newGenre = "POP"
                                    newArtistId = defaultArtistId.toString()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Criar música", color = Color.White, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredSections) { section ->
                    MusicSection(
                        section = section,
                        defaultArtistId = defaultArtistId,
                        onDeleteMusic = { musicId ->
                            scope.launch { onDeleteMusic(musicId) }
                        },
                        onUpdateMusic = { musicId, title, durationSec, genre, artistId ->
                            scope.launch { onUpdateMusic(musicId, title, durationSec, genre, artistId) }
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun MusicHeader(
    onBack: () -> Unit,
    onUserIconClick: () -> Unit,
    profileImageUrl: String?
) {
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
                        .background(Color(0xFF1AA5A2))
                        .clickable { onBack() },
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
private fun MusicSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color(0xFFB9D7D8)
            )
        },
        placeholder = {
            Text(
                text = "Encontre musicas",
                color = Color(0xFFB9D7D8),
                fontSize = 12.sp
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1E6768),
            unfocusedBorderColor = Color(0xFF1E6768),
            focusedContainerColor = Color(0xFF1E6768),
            unfocusedContainerColor = Color(0xFF1E6768),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clip(RoundedCornerShape(24.dp))
    )
}


@Composable
private fun MusicFilterRow(
    onAlbumsClick: () -> Unit,
    onArtistsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        MusicFilterPill(
            title = "Álbuns",
            isSelected = false,
            onClick = onAlbumsClick
        )
        Spacer(modifier = Modifier.width(16.dp))
        MusicFilterPill(
            title = "Artistas",
            isSelected = false,
            onClick = onArtistsClick
        )
        Spacer(modifier = Modifier.width(16.dp))
        MusicFilterPill(
            title = "Músicas",
            isSelected = true,
            onClick = {}
        )
    }
}


@Composable
private fun MusicFilterPill(
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
private fun MusicSection(
    section: MusicSectionUi,
    defaultArtistId: Long,
    onDeleteMusic: (Long) -> Unit,
    onUpdateMusic: (Long, String, Int, String, Long) -> Unit
) {
    var editingMusicId by remember { mutableStateOf<Long?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editDuration by remember { mutableStateOf("") }
    var editGenre by remember { mutableStateOf(section.title) }
    var editArtistId by remember { mutableStateOf(defaultArtistId.toString()) }
    Column {
        Text(
            text = section.title,
            color = Color.White,
            fontSize = 33.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF343438))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            section.tracks.forEach { track ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track.number.toString(),
                        color = Color(0xFFBEBEC0),
                        fontSize = 24.sp,
                        modifier = Modifier.width(26.dp)
                    )
                    Text(
                        text = track.title,
                        color = Color(0xFFEDEDED),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 10.dp)
                    )
                    Text(
                        text = track.duration,
                        color = Color(0xFFEDEDED),
                        fontSize = 24.sp
                    )
                    if (track.id != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Editar",
                            color = Color(0xFF80CBC4),
                            fontSize = 12.sp,
                            modifier = Modifier.clickable {
                                editingMusicId = track.id
                                editTitle = track.title
                                editDuration = track.duration
                                editGenre = track.genre
                                editArtistId = (track.artistId ?: defaultArtistId).toString()
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Excluir",
                            color = Color(0xFFEF9A9A),
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onDeleteMusic(track.id) }
                        )
                    }
                }
                if (editingMusicId == track.id && track.id != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Título") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = musicFieldColors()
                    )
                    OutlinedTextField(
                        value = editDuration,
                        onValueChange = { editDuration = it },
                        label = { Text("Duração") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = musicFieldColors()
                    )
                    OutlinedTextField(
                        value = editGenre,
                        onValueChange = { editGenre = it },
                        label = { Text("Gênero") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = musicFieldColors()
                    )
                    OutlinedTextField(
                        value = editArtistId,
                        onValueChange = { editArtistId = it.filter { ch -> ch.isDigit() } },
                        label = { Text("ID do artista") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = musicFieldColors()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Salvar",
                            color = Color(0xFF80CBC4),
                            fontSize = 12.sp,
                            modifier = Modifier.clickable {
                                val sec = parseMusicDurationToSeconds(editDuration) ?: 0
                                val artistId = editArtistId.toLongOrNull() ?: 0L
                                if (sec > 0 && artistId > 0L && editTitle.isNotBlank()) {
                                    onUpdateMusic(track.id, editTitle.trim(), sec, editGenre.trim(), artistId)
                                    editingMusicId = null
                                }
                            }
                        )
                        Text(
                            text = "Cancelar",
                            color = Color(0xFFBEBEC0),
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { editingMusicId = null }
                        )
                    }
                }
            }
        }
    }
}

private fun parseMusicDurationToSeconds(raw: String): Int? {
    val t = raw.trim()
    if (t.isEmpty()) return null
    val parts = t.split(":")
    return when (parts.size) {
        1 -> parts[0].toIntOrNull()
        2 -> {
            val m = parts[0].toIntOrNull() ?: return null
            val s = parts[1].toIntOrNull() ?: return null
            m * 60 + s
        }
        else -> null
    }
}

@Composable
private fun musicFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF23A7A2),
    unfocusedBorderColor = Color(0xFF656870),
    focusedLabelColor = Color(0xFF23A7A2),
    unfocusedLabelColor = Color(0xFFBEBEC0),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color(0xFF23A7A2)
)

private fun filterMusicSectionsByQuery(
    sections: List<MusicSectionUi>,
    query: String
): List<MusicSectionUi> {
    val q = query.trim()
    if (q.isBlank()) return sections
    return sections.mapNotNull { section ->
        val filteredTracks = section.tracks.filter { track ->
            track.title.contains(q, ignoreCase = true)
        }
        val sectionMatches = section.title.contains(q, ignoreCase = true)
        if (sectionMatches) section
        else if (filteredTracks.isEmpty()) null
        else section.copy(tracks = filteredTracks)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MusicScreenPreview() {
    ResonanceTheme {
        MusicScreen(
            onBack = {},
            onUserIconClick = {},
            onAlbumsClick = {},
            onArtistsClick = {},
            searchQuery = "",
            onSearchQueryChange = {},
            defaultArtistId = 1L,
            onCreateMusic = { _, _, _, _ -> true },
            onDeleteMusic = { true },
            onUpdateMusic = { _, _, _, _, _ -> true },
            sections = listOf(
                MusicSectionUi(
                    title = "Popular",
                    tracks = listOf(
                        MusicTrackUi(1L, 1L, "Popular", 1, "NUEVAYol", "3:03"),
                        MusicTrackUi(2L, 1L, "Popular", 2, "VOY A LLeVARTE PA PR", "2:36"),
                        MusicTrackUi(3L, 1L, "Popular", 3, "BAILE INoLVIDABLE", "2:36")
                    )
                )
            )
        )
    }
}
