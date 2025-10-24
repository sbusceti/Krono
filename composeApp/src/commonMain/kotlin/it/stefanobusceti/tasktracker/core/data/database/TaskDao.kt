package it.stefanobusceti.tasktracker.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("DELETE FROM task")
    suspend fun deleteAll()

    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE name = :name LIMIT 1")
    suspend fun getTaskByName(name: String): TaskEntity?

    @Query("SELECT * FROM task WHERE id = :id LIMIT 1")
    suspend fun getTaskByID(id: Int): TaskEntity?

    @Query("UPDATE task SET running = false, totalTime = :totalTime WHERE id = :id")
    suspend fun pause(id: Int, totalTime: Long?): Int

    @Query("UPDATE task SET running = false")
    suspend fun pauseAll(): Int

    @Query("UPDATE task SET running = true, startTime = :startTime WHERE id = :id")
    suspend fun resume(id: Int, startTime: Long): Int
}