package it.stefanobusceti.krono.core.domain

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun addTask(taskName: String): Result<Task>
    suspend fun deleteTask(task: Task): Result<Boolean>
    suspend fun deleteAll(): Result<Boolean>
    suspend fun pause(id: Int, totalTime: Long)
    suspend fun pauseAll()
    suspend fun resume(id: Int, startTime: Long)
    suspend fun getTaskByName(name: String): Task?
    suspend fun getTaskById(id: Int): Task?
}