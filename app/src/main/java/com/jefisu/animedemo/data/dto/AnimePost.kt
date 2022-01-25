package com.jefisu.animedemo.data.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class AnimePost(
    val name: String,
    val timestamp: String,
    val date: String,
)

@SuppressLint("SimpleDateFormat")
fun Long.toFormat(pattern: String, locale: Locale = Locale.US): String {
    val usePattern = SimpleDateFormat(pattern, locale)
    return usePattern.format(System.currentTimeMillis())
}