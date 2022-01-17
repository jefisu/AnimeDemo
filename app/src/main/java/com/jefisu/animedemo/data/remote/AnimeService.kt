package com.jefisu.animedemo.data.remote

import com.jefisu.animedemo.data.dto.AnimePost
import com.jefisu.animedemo.data.dto.AnimeResponse
import com.jefisu.animedemo.data.util.SimpleResource

interface AnimeService {

    suspend fun insertAnime(anime: AnimePost): Boolean

    suspend fun updateAnime(anime: AnimeResponse): Boolean

    suspend fun getAnimeById(id: String): AnimeResponse?

    suspend fun getAnimes(): List<AnimeResponse>

    suspend fun deleteAnime(anime: AnimeResponse): Boolean
}