package com.music.resonance.data.model

import android.media.MediaPlayer.OnTimedTextListener

class Music (
    val id:Long,
    val title: String,
    val artistId: Long,
    val duration: Int,
    val genre: String,
    val coverImageUrl: String
)