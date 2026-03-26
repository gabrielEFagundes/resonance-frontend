package com.music.resonance.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.music.resonance.R
import com.music.resonance.ui.theme.ResonanceTheme

private val ScreenBackground = Color(0xFF1E1E1E)
private val TealButton = Color(0xFF26A69A)
private val ProfileBorderBlue = Color(0xFF2196F3)

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    remoteProfileImageUrl: String? = null,
    onProfileImageUriChanged: (String) -> Unit = {},
    userName: String = "Gabriel Ehrat Fagundes",
    birthDateLabel: String = "Data de nascimento: 08/03/2008",
    registrationLabel: String = "Inscrição: 18/03/2026"
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val onImageSavedStateUpdated = rememberUpdatedState(onProfileImageUriChanged)

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
                onImageSavedStateUpdated.value(uri.toString())
            }
        }
    )

    fun openGallery() {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    val profileModel: Any = selectedImageUri
        ?: remoteProfileImageUrl?.takeIf { it.isNotBlank() }
        ?: R.drawable.user_profile_photo

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            Box(modifier = Modifier.size(188.dp)) {
                val imageModifier = Modifier
                    .align(Alignment.Center)
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(3.dp, ProfileBorderBlue, CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { openGallery() }

                Box(modifier = imageModifier) {
                    AsyncImage(
                        model = profileModel,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 4.dp, bottom = 4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(TealButton)
                        .clickable { openGallery() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✎",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = userName,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = birthDateLabel,
                color = Color.White,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = registrationLabel,
                color = Color.White,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 18.dp, top = 48.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(TealButton)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "◀",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun UserScreenPreview() {
    ResonanceTheme {
        UserScreen()
    }
}
