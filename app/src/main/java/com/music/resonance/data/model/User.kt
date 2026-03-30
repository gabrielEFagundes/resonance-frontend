package com.music.resonance.data.model

open class User (
    open val id: Long,
    open val name: String,
    open val loginDate: String,
    open val profilePictureUrl: String?
)