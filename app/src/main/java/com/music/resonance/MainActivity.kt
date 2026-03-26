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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.music.resonance.ui.screens.UserScreen
import com.music.resonance.ui.theme.ResonanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResonanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResonanceApp()
                }
            }
        }
    }
}

@Composable
private fun ResonanceApp() {
    var showProfile by remember { mutableStateOf(false) }
    BackHandler(enabled = showProfile) { showProfile = false }
    if (showProfile) {
        UserScreen(onBack = { showProfile = false })
    } else {
        MusicLibraryScreen(onNavigateToProfile = { showProfile = true })
    }
}

@Composable
fun MusicLibraryScreen(
    modifier: Modifier = Modifier,
    onNavigateToProfile: () -> Unit = {}
) {
    val albumSections = remember { sampleAlbumSections() }
    val artistSections = remember { sampleArtistSections() }
    var selectedFilter by remember { mutableStateOf("Álbuns") }
    val currentSections = if (selectedFilter == "Álbuns") albumSections else artistSections

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE6E6E6))
            .padding(horizontal = 18.dp, vertical = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF1B1D22))
                .padding(top = 20.dp)
        ) {
            HeaderSection(onUserIconClick = onNavigateToProfile)
            Spacer(modifier = Modifier.height(14.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(14.dp))
            FilterToggle(
                selected = selectedFilter,
                onSelected = { selectedFilter = it }
            )
            Spacer(modifier = Modifier.height(10.dp))
            LibrarySections(sections = currentSections)
        }
    }
}

@Composable
private fun HeaderSection(onUserIconClick: () -> Unit = {}) {
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
            Text(
                text = "ED",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
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
    onSelected: (String) -> Unit
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
private fun LibrarySections(sections: List<AlbumSection>) {
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
                        AlbumCard(album = album)
                    }
                }
            }
        }
    }
}

@Composable
private fun AlbumCard(album: AlbumItem) {
    Column(modifier = Modifier.width(140.dp)) {
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
                    name = "DeBi TIRAR MÁS FOTOs",
                    artist = "Bad Bunny",
                    tag = "BAD",
                    colors = listOf(Color(0xFF285439), Color(0xFF8BAE84))
                ),
                AlbumItem(
                    name = "Significant Other",
                    artist = "Limp Bizkit",
                    tag = "LB",
                    colors = listOf(Color(0xFF54317A), Color(0xFFE0715D))
                ),
                AlbumItem(
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
                    name = "Dr. Feelgood",
                    artist = "Motley Crue",
                    tag = "MC",
                    colors = listOf(Color(0xFF446D63), Color(0xFFC95E57))
                ),
                AlbumItem(
                    name = "Paranoid",
                    artist = "Black Sabbath",
                    tag = "BS",
                    colors = listOf(Color(0xFF19171D), Color(0xFFA53C27))
                ),
                AlbumItem(
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
                    name = "DAMN.",
                    artist = "Kendrick Lamar",
                    tag = "KL",
                    colors = listOf(Color(0xFF743B3B), Color(0xFFF2D2BF))
                ),
                AlbumItem(
                    name = "Astroworld",
                    artist = "Travis Scott",
                    tag = "TS",
                    colors = listOf(Color(0xFF8B5A2B), Color(0xFF21215B))
                ),
                AlbumItem(
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
                    name = "Bad Bunny",
                    artist = "74,4.775.730 ouvintes mensais",
                    tag = "BB",
                    colors = listOf(Color(0xFF2DBB4D), Color(0xFF064B12))
                ),
                AlbumItem(
                    name = "Sabrina Carpenter",
                    artist = "84.101.805 ouvintes mensais",
                    tag = "SC",
                    colors = listOf(Color(0xFFD39AA0), Color(0xFF7F4C53))
                ),
                AlbumItem(
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
                    name = "Michael Jackson",
                    artist = "62.136.109 ouvintes mensais",
                    tag = "MJ",
                    colors = listOf(Color(0xFFF0F0F0), Color(0xFF8C8C8C))
                ),
                AlbumItem(
                    name = "Seu Jorge",
                    artist = "6.186.869 ouvintes mensais",
                    tag = "SJ",
                    colors = listOf(Color(0xFF2B2B2B), Color(0xFF6E4F42))
                ),
                AlbumItem(
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
        MusicLibraryScreen()
    }
}