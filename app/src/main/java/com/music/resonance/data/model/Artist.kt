package com.music.resonance.data.model

import java.sql.Date

data class Artist (
    override val id: Long,
    override val name: String,
    override val loginDate: String,
    override val profilePictureUrl: String,

    val artisticName: String,
    val monthlyListeners: Long,
    val description:String

): User(
    id,
    name,
    loginDate,
    profilePictureUrl
)