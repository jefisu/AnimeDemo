package com.jefisu.animedemo.di

import com.jefisu.animedemo.data.remote.AnimeService
import com.jefisu.animedemo.data.remote.AnimeServiceImpl
import com.jefisu.animedemo.data.repository.AnimeRepository
import com.jefisu.animedemo.data.repository.AnimeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideAnimeService(): AnimeService {
        return AnimeServiceImpl(
            client = HttpClient(Android) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
                defaultRequest {
                    contentType(ContentType.Application.Json)
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideAnimeRepository(service: AnimeService): AnimeRepository {
        return AnimeRepositoryImpl(service)
    }
}