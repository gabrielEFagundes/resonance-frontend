package com.music.resonance.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
    detail: AlbumDetailUi,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlbumDetailScreen(
        detail = detail,
        onBack = onBack,
        modifier = modifier
    )
}


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
            onBack = {}
        )
    }
}
