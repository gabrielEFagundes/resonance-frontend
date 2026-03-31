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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


data class MusicTrackUi(
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
    profileImageUrl: String? = null,
    modifier: Modifier = Modifier
) {
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
            MusicSearchBar()
            Spacer(modifier = Modifier.height(14.dp))
            MusicFilterRow(
                onAlbumsClick = onAlbumsClick,
                onArtistsClick = onArtistsClick
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(sections) { section ->
                    MusicSection(section = section)
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
private fun MusicSearchBar() {
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
private fun MusicSection(section: MusicSectionUi) {
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
                }
            }
        }
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
            sections = listOf(
                MusicSectionUi(
                    title = "Popular",
                    tracks = listOf(
                        MusicTrackUi(1, "NUEVAYol", "3:03"),
                        MusicTrackUi(2, "VOY A LLeVARTE PA PR", "2:36"),
                        MusicTrackUi(3, "BAILE INoLVIDABLE", "2:36")
                    )
                )
            )
        )
    }
}
