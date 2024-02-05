package com.wenubey.crudnoteappviewui.di

import com.wenubey.crudnoteappviewui.data.remote.NoteService
import com.wenubey.crudnoteappviewui.data.remote.NoteServiceImpl
import com.wenubey.crudnoteappviewui.ui.NoteViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.json.Json
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(Logging)
            install(ContentNegotiation) {
                json(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<CoroutineDispatcher> { Dispatchers.IO }

    single<NoteService> { NoteServiceImpl(get(), get())}

    viewModel {
        NoteViewModel(get())
    }
}