package com.wenubey.crudnoteappviewui

import android.app.Application
import com.wenubey.crudnoteappviewui.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NoteApplication)
            modules(mainModule)
        }
    }
}