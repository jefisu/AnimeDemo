package com.jefisu.animedemo.data.remote

import com.jefisu.animedemo.data.dto.AnimePost
import com.jefisu.animedemo.data.dto.AnimeResponse
import com.jefisu.animedemo.data.util.SimpleResource
import io.ktor.client.*
import io.ktor.client.request.*

class AnimeServiceImpl(
    private val client: HttpClient
) : AnimeService {

    override suspend fun insertAnime(anime: AnimePost?): Boolean {
       return client.post {
           url("http://192.168.0.2:8080/api/anime")
           anime?.let {
               body = it
           }
       }
    }

    override suspend fun updateAnime(anime: AnimeResponse): Boolean {
        return client.put {
            url("http://192.168.0.2:8080/api/anime")
            body = anime
        }
    }

    override suspend fun getAnimeById(id: String): AnimeResponse? {
        return client.get("http://192.168.0.2:8080/api/anime/$id")
    }

    override suspend fun getAnimes(): List<AnimeResponse> {
        return client.get("http://192.168.0.2:8080/api/animes")
    }

    override suspend fun deleteAnime(anime: AnimeResponse): Boolean {
        return client.delete {
            url("http://192.168.0.2:8080/api/anime")
            body = anime
        }
    }
}