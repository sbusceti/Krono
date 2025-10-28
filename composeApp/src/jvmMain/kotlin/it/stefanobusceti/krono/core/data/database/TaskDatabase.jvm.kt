package it.stefanobusceti.krono.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

actual object DatabaseConstructor : RoomDatabaseConstructor<TaskDatabase> {
    // 1. Specifica la directory e il nome del file del DB
    private const val DB_NAME = "tasks.db"

    actual override fun initialize(): TaskDatabase {

        // 2. Determina la directory dove salvare il file del DB.
        // Un buon posto è la directory home dell'utente.
        val dbDir = File(System.getProperty("user.home"), "TaskDatabase")

        // Crea la directory se non esiste
        if (!dbDir.exists()) {
            dbDir.mkdirs()
        }

        val dbFile = File(dbDir, DB_NAME)

        // 3. Usa Room.databaseBuilder con il percorso assoluto
        return Room.databaseBuilder<TaskDatabase>(
            name = dbFile.absolutePath,
        )
            // 4. Aggiungi il driver SQLite necessario per KMP su JVM
            .setDriver(BundledSQLiteDriver())
            // 5. Configurazione per Coroutine (opzionale, ma consigliata)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}