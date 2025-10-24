package it.stefanobusceti.tasktracker

import android.app.Application
import it.stefanobusceti.tasktracker.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class TaskTrackerApplication : Application() {
    init {
        initKoin {
            androidContext(this@TaskTrackerApplication)
        }
    }
}