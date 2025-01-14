package com.ribod.kdraw.di

import com.ribod.kdraw.data.DrawRepositoryImpl
import com.ribod.kdraw.data.LinesRepositoryImpl
import com.ribod.kdraw.domain.DrawRepository
import com.ribod.kdraw.domain.LinesRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTP
                    host = "localhost:8090"
                }
            }
        }
    }
    factory<DrawRepository> { DrawRepositoryImpl(get()) }
    factory<LinesRepository> { LinesRepositoryImpl(get()) }
}