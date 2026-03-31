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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.resonance.ui.theme.ResonanceTheme
import kotlinx.coroutines.launch


data class PlaylistUi(
    val id: String,
    val title: String,
    val trackCount: Int,
    val owner: String
)


@Composable
fun PlaylistScreen(
    playlists: List<PlaylistUi>,
    onBack: () -> Unit,
    isCreateMode: Boolean = false,
    defaultArtistId: Long = 1L,
    onCreatePlaylist: suspend (title: String, musicIds: List<Long>) -> Boolean = { _, _ -> false },
    onUpdatePlaylist: suspend (id: String, title: String) -> Boolean = { _, _ -> false },
    onDeletePlaylist: suspend (id: String) -> Boolean = { false },
    onAddMusicToPlaylist: suspend (playlistId: String, title: String, durationSeconds: Int, genre: String, artistId: Long) -> Boolean =
        { _, _, _, _, _ -> false },
    musicOptions: List<Pair<Long, String>> = emptyList(),
    createMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var playlistTitle by remember { mutableStateOf("") }
    var editingPlaylistId by remember { mutableStateOf<String?>(null) }
    var editingPlaylistTitle by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) }
    var selectedMusicIds by remember { mutableStateOf(listOf<Long>()) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1B1D22))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF23A7A2))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = if (isCreateMode) "Criar Playlist" else "Playlists",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isCreateMode) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (step == 1) {
                        Text("1/3 - Início", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(
                            "Vamos criar sua playlist em 3 passos.",
                            color = Color(0xFFBEBEC0),
                            fontSize = 13.sp
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF23A7A2))
                                .clickable { step = 2 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Começar", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    } else if (step == 2) {
                        Text("2/3 - Criar", color = Color.White, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = playlistTitle,
                            onValueChange = { playlistTitle = it },
                            label = { Text("Nome da Playlist") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = fieldColors()
                        )
                        musicOptions.take(6).forEach { (musicId, musicName) ->
                            val selected = selectedMusicIds.contains(musicId)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (selected) Color(0xFF23A7A2) else Color(0xFF343438))
                                    .clickable {
                                        selectedMusicIds = if (selected) selectedMusicIds - musicId else selectedMusicIds + musicId
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selected) "✓" else "○",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = musicName, color = Color.White, fontSize = 13.sp)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(CircleShape)
                                .background(if (playlistTitle.isBlank()) Color(0xFF5C5F66) else Color(0xFF23A7A2))
                                .clickable(enabled = playlistTitle.isNotBlank()) {
                                    scope.launch {
                                        val ok = onCreatePlaylist(playlistTitle.trim(), selectedMusicIds)
                                        if (ok) {
                                            feedbackMessage = "Sua playlist foi postada com sucesso!"
                                            step = 3
                                        } else {
                                            feedbackMessage = "Falha ao criar playlist na API."
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Publicar", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Text("3/3 - Concluído", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(
                            feedbackMessage ?: createMessage ?: "Playlist criada com sucesso!",
                            color = Color(0xFFBEEEEB),
                            fontSize = 14.sp
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF23A7A2))
                                .clickable {
                                    step = 1
                                    selectedMusicIds = emptyList()
                                    playlistTitle = ""
                                    onBack()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("OK!", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    createMessage?.takeIf { it.isNotBlank() && step != 3 }?.let { msg ->
                        Text(
                            text = msg,
                            color = Color(0xFFBEEEEB),
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(playlists, key = { it.id }) { playlist ->
                        var newTrackTitle by remember(playlist.id) { mutableStateOf("") }
                        var newDuration by remember(playlist.id) { mutableStateOf("") }
                        var newGenre by remember(playlist.id) { mutableStateOf("POP") }
                        var newArtistId by remember(playlist.id) { mutableStateOf(defaultArtistId.toString()) }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF343438))
                                .padding(14.dp)
                        ) {
                            if (editingPlaylistId == playlist.id) {
                                OutlinedTextField(
                                    value = editingPlaylistTitle,
                                    onValueChange = { editingPlaylistTitle = it },
                                    label = { Text("Editar título") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = fieldColors()
                                )
                            } else {
                                Text(
                                    text = playlist.title,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${playlist.trackCount} músicas",
                                color = Color(0xFFBEBEC0),
                                fontSize = 13.sp
                            )
                            Text(
                                text = "por ${playlist.owner}",
                                color = Color(0xFF9BA1AB),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ActionChip(
                                    title = if (editingPlaylistId == playlist.id) "Salvar" else "Editar",
                                    onClick = {
                                        if (editingPlaylistId == playlist.id) {
                                            scope.launch {
                                                val ok = onUpdatePlaylist(playlist.id, editingPlaylistTitle.trim())
                                                if (ok) editingPlaylistId = null
                                            }
                                        } else {
                                            editingPlaylistId = playlist.id
                                            editingPlaylistTitle = playlist.title
                                        }
                                    }
                                )
                                ActionChip(
                                    title = "Excluir",
                                    onClick = {
                                        scope.launch { onDeletePlaylist(playlist.id) }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Nova faixa", color = Color(0xFFBEBEC0), fontSize = 12.sp)
                            OutlinedTextField(
                                value = newTrackTitle,
                                onValueChange = { newTrackTitle = it },
                                label = { Text("Titulo da musica") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = fieldColors()
                            )
                            OutlinedTextField(
                                value = newDuration,
                                onValueChange = { newDuration = it },
                                label = { Text("Duracao (ex: 3:45 ou 180 seg)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = fieldColors()
                            )
                            OutlinedTextField(
                                value = newGenre,
                                onValueChange = { newGenre = it },
                                label = { Text("Genero") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = fieldColors()
                            )
                            OutlinedTextField(
                                value = newArtistId,
                                onValueChange = { newArtistId = it.filter { ch -> ch.isDigit() } },
                                label = { Text("ID do artista") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = fieldColors()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFF1E6768))
                                    .clickable(enabled = newTrackTitle.isNotBlank()) {
                                        val sec = parseDurationToSeconds(newDuration) ?: 0
                                        val artistId = newArtistId.toLongOrNull() ?: 0L
                                        if (sec <= 0) return@clickable
                                        if (artistId <= 0L) return@clickable
                                        scope.launch {
                                            if (onAddMusicToPlaylist(playlist.id, newTrackTitle.trim(), sec, newGenre.trim(), artistId)) {
                                                newTrackTitle = ""
                                                newDuration = ""
                                                newGenre = "POP"
                                                newArtistId = defaultArtistId.toString()
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Adicionar faixa", color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun parseDurationToSeconds(raw: String): Int? {
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
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF23A7A2),
    unfocusedBorderColor = Color(0xFF656870),
    focusedLabelColor = Color(0xFF23A7A2),
    unfocusedLabelColor = Color(0xFFBEBEC0),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color(0xFF23A7A2)
)

@Composable
private fun ActionChip(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFF23A7A2))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PlaylistScreenPreview() {
    ResonanceTheme {
        PlaylistScreen(
            playlists = listOf(
                PlaylistUi("1", "Road Trip", 20, "Eduardo"),
                PlaylistUi("2", "Rock Classics", 35, "Gabriel")
            ),
            onBack = {}
        )
    }
}
