package com.music.resonance.ui.screens


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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.resonance.ui.theme.ResonanceTheme
import kotlinx.coroutines.launch


data class AlbumTrackUi(
    val number: Int,
    val title: String,
    val duration: String
)


data class AlbumDetailUi(
    val id: String,
    val title: String,
    val artist: String,
    val year: Int,
    val coverGradientStart: Long,
    val coverGradientEnd: Long,
    val tracks: List<AlbumTrackUi>
)


private val secondaryTrackText = Color(0xFF9BA1AB)


fun sampleAlbumDetailById(id: String): AlbumDetailUi? = albumDetailsById[id]


private val albumDetailsById: Map<String, AlbumDetailUi> = buildMap {
    put(
        "debi-tirar-mas-fotos",
        AlbumDetailUi(
            id = "debi-tirar-mas-fotos",
            title = "DeBÍ TiRAR MáS FOToS",
            artist = "Bad Bunny",
            year = 2025,
            coverGradientStart = 0xFF285439L,
            coverGradientEnd = 0xFF8BAE84L,
            tracks = listOf(
                AlbumTrackUi(1, "NUEVAYol", "3:03"),
                AlbumTrackUi(2, "VOY A LLeVARTE PA PR", "2:36"),
                AlbumTrackUi(3, "BAILE INoLVIDABLE", "2:36"),
                AlbumTrackUi(4, "PERFuMITO NUEVO", "3:07"),
                AlbumTrackUi(5, "WELTITA", "3:55"),
                AlbumTrackUi(6, "VeLDÁ", "3:55"),
                AlbumTrackUi(7, "EL CLÚB", "3:42"),
                AlbumTrackUi(8, "KETU TeCRÉ", "4:10"),
                AlbumTrackUi(9, "BOKeTE", "3:35")
            )
        )
    )
    put(
        "significant-other",
        AlbumDetailUi(
            id = "significant-other",
            title = "Significant Other",
            artist = "Limp Bizkit",
            year = 1999,
            coverGradientStart = 0xFF54317AL,
            coverGradientEnd = 0xFFE0715DL,
            tracks = defaultTracksFor("Significant Other")
        )
    )
    put(
        "random-access-memories",
        AlbumDetailUi(
            id = "random-access-memories",
            title = "Random Access Memories",
            artist = "Daft Punk",
            year = 2013,
            coverGradientStart = 0xFF222634L,
            coverGradientEnd = 0xFFB88E2FL,
            tracks = defaultTracksFor("Random Access Memories")
        )
    )
    put(
        "dr-feelgood",
        AlbumDetailUi(
            id = "dr-feelgood",
            title = "Dr. Feelgood",
            artist = "Mötley Crüe",
            year = 1989,
            coverGradientStart = 0xFF446D63L,
            coverGradientEnd = 0xFFC95E57L,
            tracks = defaultTracksFor("Dr. Feelgood")
        )
    )
    put(
        "paranoid",
        AlbumDetailUi(
            id = "paranoid",
            title = "Paranoid",
            artist = "Black Sabbath",
            year = 1970,
            coverGradientStart = 0xFF19171DL,
            coverGradientEnd = 0xFFA53C27L,
            tracks = defaultTracksFor("Paranoid")
        )
    )
    put(
        "back-in-black",
        AlbumDetailUi(
            id = "back-in-black",
            title = "Back in Black",
            artist = "AC/DC",
            year = 1980,
            coverGradientStart = 0xFF1A1A1AL,
            coverGradientEnd = 0xFF6E6E6EL,
            tracks = defaultTracksFor("Back in Black")
        )
    )
    put(
        "damn",
        AlbumDetailUi(
            id = "damn",
            title = "DAMN.",
            artist = "Kendrick Lamar",
            year = 2017,
            coverGradientStart = 0xFF743B3BL,
            coverGradientEnd = 0xFFF2D2BFL,
            tracks = defaultTracksFor("DAMN.")
        )
    )
    put(
        "astroworld",
        AlbumDetailUi(
            id = "astroworld",
            title = "Astroworld",
            artist = "Travis Scott",
            year = 2018,
            coverGradientStart = 0xFF8B5A2BL,
            coverGradientEnd = 0xFF21215BL,
            tracks = defaultTracksFor("Astroworld")
        )
    )
    put(
        "eminem-show",
        AlbumDetailUi(
            id = "eminem-show",
            title = "The Eminem Show",
            artist = "Eminem",
            year = 2002,
            coverGradientStart = 0xFF1E6A73L,
            coverGradientEnd = 0xFFBBE6E8L,
            tracks = defaultTracksFor("The Eminem Show")
        )
    )
    put(
        "un-verano-sin-ti",
        AlbumDetailUi(
            id = "un-verano-sin-ti",
            title = "Un Verano Sin Ti",
            artist = "Bad Bunny",
            year = 2022,
            coverGradientStart = 0xFF7F1D1DL,
            coverGradientEnd = 0xFFFECADAL,
            tracks = defaultTracksFor("Un Verano Sin Ti")
        )
    )
    put(
        "nadie-sabe",
        AlbumDetailUi(
            id = "nadie-sabe",
            title = "Nadie Sabe Lo Que Pasará Mañana",
            artist = "Bad Bunny",
            year = 2023,
            coverGradientStart = 0xFF1E293BL,
            coverGradientEnd = 0xFF475569L,
            tracks = defaultTracksFor("Nadie Sabe Lo Que Pasará Mañana")
        )
    )
    put(
        "artist-demo-album-1",
        AlbumDetailUi(
            id = "artist-demo-album-1",
            title = "Seleção — Vol. 1",
            artist = "Artista",
            year = 2024,
            coverGradientStart = 0xFF334155L,
            coverGradientEnd = 0xFF0F172AL,
            tracks = defaultTracksFor("Seleção — Vol. 1")
        )
    )
    put(
        "artist-demo-album-2",
        AlbumDetailUi(
            id = "artist-demo-album-2",
            title = "Ao vivo",
            artist = "Artista",
            year = 2023,
            coverGradientStart = 0xFF57534EL,
            coverGradientEnd = 0xFF78716CL,
            tracks = defaultTracksFor("Ao vivo")
        )
    )
}


private fun defaultTracksFor(albumTitle: String): List<AlbumTrackUi> = listOf(
    AlbumTrackUi(1, "$albumTitle — faixa 1", "3:12"),
    AlbumTrackUi(2, "$albumTitle — faixa 2", "3:45"),
    AlbumTrackUi(3, "$albumTitle — faixa 3", "4:01")
)


@Composable
fun AlbumDetailScreen(
    detail: AlbumDetailUi,
    onUpdateAlbum: suspend (id: String, title: String, releaseYear: Int) -> Boolean,
    onDeleteAlbum: suspend (id: String) -> Boolean,
    onAddMusicToAlbum: suspend (title: String, durationSeconds: Int, genre: String) -> Boolean = { _, _, _ -> false },
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var editing by remember { mutableStateOf(false) }
    var editTitle by remember(detail.id) { mutableStateOf(detail.title) }
    var editYear by remember(detail.id) { mutableStateOf(detail.year.toString()) }
    var newTrackTitle by remember(detail.id) { mutableStateOf("") }
    var newDuration by remember(detail.id) { mutableStateOf("") }
    var newGenre by remember(detail.id) { mutableStateOf("POP") }
    val coverStart = Color(detail.coverGradientStart.toInt())
    val coverEnd = Color(detail.coverGradientEnd.toInt())


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE6E6E6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1B1D22))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(22.dp))
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
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(coverStart, coverEnd)
                                )
                            )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = detail.title,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${detail.artist} ${detail.year}",
                        color = Color(0xFF9BA1AB),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (editing) {
                        OutlinedTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            label = { Text("Título") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            colors = formColors()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editYear,
                            onValueChange = { editYear = it.filter { ch -> ch.isDigit() }.take(4) },
                            label = { Text("Ano") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            colors = formColors()
                        )
                    }
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionChip(
                            title = if (editing) "Salvar" else "Editar",
                            onClick = {
                                if (editing) {
                                    scope.launch {
                                        onUpdateAlbum(detail.id, editTitle, editYear.toIntOrNull() ?: detail.year)
                                        editing = false
                                    }
                                } else {
                                    editing = true
                                }
                            }
                        )
                        ActionChip(
                            title = "Excluir",
                            onClick = {
                                scope.launch {
                                    if (onDeleteAlbum(detail.id)) onBack()
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Adicionar faixa ao album",
                        color = Color(0xFFBEBEC0),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newTrackTitle,
                        onValueChange = { newTrackTitle = it },
                        label = { Text("Titulo") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        colors = formColors()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = newDuration,
                        onValueChange = { newDuration = it },
                        label = { Text("Duracao (3:45 ou segundos)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        colors = formColors()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = newGenre,
                        onValueChange = { newGenre = it },
                        label = { Text("Genero") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        colors = formColors()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E6768))
                            .clickable(enabled = newTrackTitle.isNotBlank()) {
                                val sec = parseAlbumDurationToSeconds(newDuration) ?: 0
                                if (sec <= 0) return@clickable
                                scope.launch {
                                    if (onAddMusicToAlbum(newTrackTitle.trim(), sec, newGenre.trim())) {
                                        newTrackTitle = ""
                                        newDuration = ""
                                        newGenre = "POP"
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Adicionar faixa", color = Color.White, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items(detail.tracks) { track ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = track.number.toString(),
                            color = secondaryTrackText,
                            fontSize = 15.sp,
                            modifier = Modifier.width(28.dp)
                        )
                        Text(
                            text = track.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp, end = 12.dp)
                        )
                        Text(
                            text = track.duration,
                            color = secondaryTrackText,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumScreen(
    detail: AlbumDetailUi?,
    onBack: () -> Unit,
    isCreateMode: Boolean = false,
    onCreateAlbum: suspend (title: String, releaseYear: Int, musicIds: List<Long>) -> Boolean = { _, _, _ -> false },
    onUpdateAlbum: suspend (id: String, title: String, releaseYear: Int) -> Boolean = { _, _, _ -> false },
    onDeleteAlbum: suspend (id: String) -> Boolean = { false },
    onAddMusicToAlbum: suspend (title: String, durationSeconds: Int, genre: String) -> Boolean = { _, _, _ -> false },
    musicOptions: List<Pair<Long, String>> = emptyList(),
    createMessage: String? = null,
    modifier: Modifier = Modifier
) {
    if (isCreateMode) {
        CreateAlbumScreen(
            onBack = onBack,
            onCreateAlbum = onCreateAlbum,
            createMessage = createMessage,
            musicOptions = musicOptions,
            modifier = modifier
        )
    } else {
        val safeDetail = detail ?: return
        AlbumDetailScreen(
            detail = safeDetail,
            onUpdateAlbum = onUpdateAlbum,
            onDeleteAlbum = onDeleteAlbum,
            onAddMusicToAlbum = onAddMusicToAlbum,
            onBack = onBack,
            modifier = modifier
        )
    }
}

@Composable
private fun CreateAlbumScreen(
    onBack: () -> Unit,
    onCreateAlbum: suspend (title: String, releaseYear: Int, musicIds: List<Long>) -> Boolean,
    musicOptions: List<Pair<Long, String>>,
    createMessage: String?,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var albumTitle by remember { mutableStateOf("") }
    var releaseYearText by remember { mutableStateOf("") }
    var selectedMusicIds by remember { mutableStateOf(listOf<Long>()) }
    var step by remember { mutableStateOf(1) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val releaseYear = releaseYearText.toIntOrNull()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1B1D22))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
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
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Criar Álbum",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (step == 1) {
                Text("1/3 - Início", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Vamos criar seu álbum em 3 passos.", color = Color(0xFFBEBEC0), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                ActionButton("Começar") { step = 2 }
            } else if (step == 2) {
                Text("2/3 - Criar", color = Color.White, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = albumTitle,
                    onValueChange = { albumTitle = it },
                    label = { Text("Nome do Álbum") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = formColors()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = releaseYearText,
                    onValueChange = { releaseYearText = it.filter { ch -> ch.isDigit() }.take(4) },
                    label = { Text("Ano de lançamento") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = formColors()
                )
                Spacer(modifier = Modifier.height(12.dp))
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
                        Text(if (selected) "✓" else "○", color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(musicName, color = Color.White, fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                ActionButton(
                    title = "Publicar",
                    enabled = albumTitle.isNotBlank() && releaseYear != null
                ) {
                    scope.launch {
                        val ok = onCreateAlbum(albumTitle.trim(), releaseYear ?: 0, selectedMusicIds)
                        if (ok) {
                            feedbackMessage = "Seu álbum foi postado com sucesso!"
                            step = 3
                        } else {
                            feedbackMessage = "Falha ao criar álbum na API."
                        }
                    }
                }
            } else {
                Text("3/3 - Concluído", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = feedbackMessage ?: createMessage ?: "Álbum criado com sucesso!",
                    color = Color(0xFFBEEEEB),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                ActionButton("OK!") {
                    step = 1
                    albumTitle = ""
                    releaseYearText = ""
                    selectedMusicIds = emptyList()
                    onBack()
                }
            }
        }
    }
}

@Composable
private fun ActionButton(title: String, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (enabled) Color(0xFF23A7A2) else Color(0xFF5C5F66))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

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

private fun parseAlbumDurationToSeconds(raw: String): Int? {
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
private fun formColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF23A7A2),
    unfocusedBorderColor = Color(0xFF656870),
    focusedLabelColor = Color(0xFF23A7A2),
    unfocusedLabelColor = Color(0xFFBEBEC0),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color(0xFF23A7A2)
)


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AlbumDetailScreenPreview() {
    ResonanceTheme {
        AlbumDetailScreen(
            detail = AlbumDetailUi(
                id = "preview",
                title = "DeBÍ TiRAR MáS FOToS",
                artist = "Bad Bunny",
                year = 2025,
                coverGradientStart = 0xFF285439L,
                coverGradientEnd = 0xFF8BAE84L,
                tracks = listOf(
                    AlbumTrackUi(1, "NUEVAYol", "3:03"),
                    AlbumTrackUi(2, "VOY A LLeVARTE PA PR", "2:36")
                )
            ),
            onUpdateAlbum = { _, _, _ -> true },
            onDeleteAlbum = { true },
            onAddMusicToAlbum = { _, _, _ -> true },
            onBack = {}
        )
    }
}
