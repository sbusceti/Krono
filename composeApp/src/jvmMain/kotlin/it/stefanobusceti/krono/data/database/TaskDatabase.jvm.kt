package it.stefanobusceti.krono.data.database

import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

actual object DatabaseConstructor : RoomDatabaseConstructor<TaskDatabase> {

    actual override fun initialize(): TaskDatabase {
        return Room.databaseBuilder<TaskDatabase>(
            name = getDatabaseFile().absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

private fun getDatabaseFile(): File {
    val os = System.getProperty("os.name").lowercase()
    val userHome = System.getProperty("user.home")
    val appName = "Krono"

    val baseDir = when {
        os.contains("mac") || os.contains("darwin") -> {
            // macOS
            File(userHome, "Library/Application Support/$appName")
        }

        os.contains("win") -> {
            // Windows
            val appData = System.getenv("LOCALAPPDATA")
                ?: File(userHome, "AppData/Local").absolutePath
            File(appData, appName)
        }

        else -> {
            // Linux
            val xdgDataHome = System.getenv("XDG_DATA_HOME")
                ?: File(userHome, ".local/share").absolutePath
            File(xdgDataHome, appName)
        }
    }

    
    if (!baseDir.exists()) {
        baseDir.mkdirs()
    }

    return File(baseDir, "tasks.db")
}