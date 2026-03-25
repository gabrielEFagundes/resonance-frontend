package com.music.resonance.data.model

import java.sql.Date

open class User (
    open val id: Long,
    open val name: String,
    open val loginDate: Date
)