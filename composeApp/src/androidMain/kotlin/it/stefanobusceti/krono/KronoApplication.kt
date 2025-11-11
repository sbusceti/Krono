package it.stefanobusceti.krono

import android.app.Application
import it.stefanobusceti.krono.di.initKoin
import org.koin.android.ext.koin.androidContext

class KronoApplication : Application() {
    init {
        initKoin {
            androidContext(this@KronoApplication)
        }
    }
}