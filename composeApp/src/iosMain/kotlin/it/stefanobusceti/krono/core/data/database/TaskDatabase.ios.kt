package it.stefanobusceti.krono.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import platform.Foundation.NSHomeDirectory

actual object DatabaseConstructor : RoomDatabaseConstructor<TaskDatabase> {
    actual override fun initialize(): TaskDatabase {
        val dbFilePath = NSHomeDirectory() + "/tasks.db"

        return Room.databaseBuilder<TaskDatabase>(
            name = dbFilePath
        ).build()
    }
}