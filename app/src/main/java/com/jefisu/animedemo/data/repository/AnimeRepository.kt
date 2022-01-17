package com.jefisu.animedemo.data.repository

import com.jefisu.animedemo.data.dto.AnimePost
import com.jefisu.animedemo.data.dto.AnimeResponse
import com.jefisu.animedemo.data.util.Resource
import com.jefisu.animedemo.data.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    suspend fun insertAnime(anime: AnimePost): Resource<Boolean>

    suspend fun updateAnime(anime: AnimeResponse): Resource<Boolean>

    suspend fun getAnimeById(id: String): AnimeResponse?

    suspend fun getAnimes(): Flow<Resource<List<AnimeResponse>>>

    suspend fun deleteAnime(anime: AnimeResponse): Resource<Boolean>
}