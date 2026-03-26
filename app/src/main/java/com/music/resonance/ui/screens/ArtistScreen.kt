package com.music.resonance.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.resonance.ui.theme.ResonanceTheme


data class ArtistAlbumTileUi(
    val albumId: String,
    val displayTitle: String,
    val coverGradientStart: Long,
    val coverGradientEnd: Long
)


data class ArtistProfileUi(
    val id: String,
    val name: String,
    val listenersLine: String,
    val bioIntro: String,
    val bioExtra: String,
    val heroGradientStart: Long,
    val heroGradientEnd: Long,
    val albums: List<ArtistAlbumTileUi>
)


private val accentTeal = Color(0xFF23A7A2)
private val secondaryText = Color(0xFF9BA1AB)
private val bioText = Color(0xFFB0B8C0)


fun sampleArtistProfileById(id: String): ArtistProfileUi? = artistProfilesById[id]


private val artistProfilesById: Map<String, ArtistProfileUi> = mapOf(
    "artist-bad-bunny" to ArtistProfileUi(
        id = "artist-bad-bunny",
        name = "Bad Bunny",
        listenersLine = "114.074.578 ouvintes mensais",
        bioIntro = "Bad Bunny é um artista global multi-platina e vencedor de 7 GRAMMY®, cujo som que desafia gêneros remodelou a música e a cultura contemporâneas.",
        bioExtra = " Com lançamentos que cruzam reggaeton, trap e rock latino, ele consolidou turnês recordistas e colaborações que definiram a última década.",
        heroGradientStart = 0xFF2DBB4DL,
        heroGradientEnd = 0xFF064B12L,
        albums = listOf(
            ArtistAlbumTileUi(
                albumId = "debi-tirar-mas-fotos",
                displayTitle = "DeBÍ TIRAR Más FOTOS",
                coverGradientStart = 0xFF285439L,
                coverGradientEnd = 0xFF8BAE84L
            ),
            ArtistAlbumTileUi(
                albumId = "un-verano-sin-ti",
                displayTitle = "Un Verano Sin Ti",
                coverGradientStart = 0xFF7F1D1DL,
                coverGradientEnd = 0xFFFECADAL
            ),
            ArtistAlbumTileUi(
                albumId = "nadie-sabe",
                displayTitle = "Nadie Sabe Lo Que Pasará Mañana",
                coverGradientStart = 0xFF1E293BL,
                coverGradientEnd = 0xFF475569L
            )
        )
    ),
    "artist-sabrina" to genericArtistProfile(
        id = "artist-sabrina",
        name = "Sabrina Carpenter",
        listenersLine = "84.101.805 ouvintes mensais",
        heroStart = 0xFFD39AA0L,
        heroEnd = 0xFF7F4C53L
    ),
    "artist-weeknd" to genericArtistProfile(
        id = "artist-weeknd",
        name = "The Weeknd",
        listenersLine = "111.642.112 ouvintes mensais",
        heroStart = 0xFF121212L,
        heroEnd = 0xFF4D4D4DL
    ),
    "artist-mj" to genericArtistProfile(
        id = "artist-mj",
        name = "Michael Jackson",
        listenersLine = "62.136.109 ouvintes mensais",
        heroStart = 0xFFF0F0F0L,
        heroEnd = 0xFF8C8C8CL
    ),
    "artist-seu-jorge" to genericArtistProfile(
        id = "artist-seu-jorge",
        name = "Seu Jorge",
        listenersLine = "6.186.869 ouvintes mensais",
        heroStart = 0xFF2B2B2BL,
        heroEnd = 0xFF6E4F42L
    ),
    "artist-tim-maia" to genericArtistProfile(
        id = "artist-tim-maia",
        name = "Tim Maia",
        listenersLine = "5.220.441 ouvintes mensais",
        heroStart = 0xFF513722L,
        heroEnd = 0xFFD29462L
    )
)


private fun genericArtistProfile(
    id: String,
    name: String,
    listenersLine: String,
    heroStart: Long,
    heroEnd: Long
): ArtistProfileUi {
    val intro =
        "$name é um dos artistas em destaque na Resonance, com milhões de ouvintes mensais e um catálogo que mistura hits e descobertas."
    val extra =
        " A trajetória inclui álbuns de estúdio, participações e momentos ao vivo que marcaram fãs em várias gerações."
    return ArtistProfileUi(
        id = id,
        name = name,
        listenersLine = listenersLine,
        bioIntro = intro,
        bioExtra = extra,
        heroGradientStart = heroStart,
        heroGradientEnd = heroEnd,
        albums = listOf(
            ArtistAlbumTileUi(
                albumId = "artist-demo-album-1",
                displayTitle = "Seleção — Vol. 1",
                coverGradientStart = 0xFF334155L,
                coverGradientEnd = 0xFF0F172AL
            ),
            ArtistAlbumTileUi(
                albumId = "artist-demo-album-2",
                displayTitle = "Ao vivo",
                coverGradientStart = 0xFF57534EL,
                coverGradientEnd = 0xFF78716CL
            )
        )
    )
}


@Composable
fun ArtistProfileScreen(
    profile: ArtistProfileUi,
    onBack: () -> Unit,
    onAlbumOpen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var bioExpanded by remember(profile.id) { mutableStateOf(false) }
    val heroStart = Color(profile.heroGradientStart.toInt())
    val heroEnd = Color(profile.heroGradientEnd.toInt())


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE6E6E6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 28.dp)
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
                                .background(accentTeal)
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(heroStart, heroEnd)
                                )
                            )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = profile.name,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = profile.listenersLine,
                        color = secondaryText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = profile.bioIntro +
                                if (bioExpanded) profile.bioExtra else "",
                        color = bioText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .clickable { bioExpanded = !bioExpanded },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (bioExpanded) "Ler menos" else "Ler mais",
                            color = accentTeal,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = accentTeal,
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(if (bioExpanded) 180f else 0f)
                        )
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = "Álbuns",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        profile.albums.forEach { tile ->
                            ArtistAlbumTile(
                                tile = tile,
                                onClick = { onAlbumOpen(tile.albumId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArtistScreen(
    profile: ArtistProfileUi,
    onBack: () -> Unit,
    onAlbumOpen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ArtistProfileScreen(
        profile = profile,
        onBack = onBack,
        onAlbumOpen = onAlbumOpen,
        modifier = modifier
    )
}


@Composable
private fun ArtistAlbumTile(
    tile: ArtistAlbumTileUi,
    onClick: () -> Unit
) {
    val start = Color(tile.coverGradientStart.toInt())
    val end = Color(tile.coverGradientEnd.toInt())
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(listOf(start, end)))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = tile.displayTitle,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            lineHeight = 14.sp
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ArtistProfileScreenPreview() {
    ResonanceTheme {
        sampleArtistProfileById("artist-bad-bunny")?.let { profile ->
            ArtistProfileScreen(
                profile = profile,
                onBack = {},
                onAlbumOpen = {}
            )
        }
    }
}

