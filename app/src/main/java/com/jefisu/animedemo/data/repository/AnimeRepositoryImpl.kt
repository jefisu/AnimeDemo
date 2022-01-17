package com.jefisu.animedemo.data.repository

import com.jefisu.animedemo.data.dto.AnimePost
import com.jefisu.animedemo.data.dto.AnimeResponse
import com.jefisu.animedemo.data.remote.AnimeService
import com.jefisu.animedemo.data.util.Resource
import io.ktor.client.features.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnimeRepositoryImpl(
    private val service: AnimeService
) : AnimeRepository {

    override suspend fun insertAnime(anime: AnimePost): Resource<Boolean> {
        return try {
            Resource.Success(
                service.insertAnime(anime)
            )
        } catch (e: RedirectResponseException) {
            Resource.Error(
                uiText = e.message.toString(),
            )
        } catch (e: ClientRequestException) {
            Resource.Error(
                uiText = e.message,
            )
        } catch (e: ServerResponseException) {
            Resource.Error(
                uiText = e.message.toString(),
            )
        }
    }

    override suspend fun updateAnime(anime: AnimeResponse): Resource<Boolean> {
        return try {
            Resource.Success(
                service.updateAnime(anime)
            )
        } catch (e: RedirectResponseException) {
            Resource.Error(
                uiText = e.message.toString(),
                data = false
            )
        } catch (e: ClientRequestException) {
            Resource.Error(
                uiText = e.message,
                data = false
            )
        } catch (e: ServerResponseException) {
            Resource.Error(
                uiText = e.message.toString(),
                data = false
            )
        }
    }

    override suspend fun getAnimeById(id: String): AnimeResponse? {
        return try {
            service.getAnimeById(id)
        } catch (e: RedirectResponseException) {
            println("Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Error: ${e.response.status}")
            null
        }
    }

    override suspend fun getAnimes(): Flow<Resource<List<AnimeResponse>>> {
        val result = service.getAnimes()
        return flow {
            try {
                emit(Resource.Success(result))
            } catch (e: RedirectResponseException) {
                emit(
                    Resource.Error(
                        uiText = e.message.toString(),
                        data = result
                    )
                )
            } catch (e: ClientRequestException) {
                emit(
                    Resource.Error(
                        uiText = e.message,
                        data = result
                    )
                )
            } catch (e: ServerResponseException) {
               emit(
                   Resource.Error(
                       uiText = e.message.toString(),
                       data = result
                   )
               )
            }
        }
    }

    override suspend fun deleteAnime(anime: AnimeResponse): Resource<Boolean> {
        return try {
            Resource.Success(
                service.deleteAnime(anime)
            )
        } catch (e: RedirectResponseException) {
            Resource.Error(uiText = e.message.toString())
        } catch (e: ClientRequestException) {
            Resource.Error(uiText = e.message)
        } catch (e: ServerResponseException) {
            Resource.Error(uiText = e.message.toString())
        }
    }
}