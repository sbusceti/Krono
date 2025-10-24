package it.stefanobusceti.tasktracker.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.component.KoinComponent

actual object DatabaseConstructor : RoomDatabaseConstructor<TaskDatabase> {

    actual override fun initialize(): TaskDatabase {
        val koinComponent: KoinComponent = object : KoinComponent {}
        val context: Context = koinComponent.getKoin().get()

        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("tasks.db")

        return Room.databaseBuilder<TaskDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}