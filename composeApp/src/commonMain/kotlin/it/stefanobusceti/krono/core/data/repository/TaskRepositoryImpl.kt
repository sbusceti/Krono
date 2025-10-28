package it.stefanobusceti.krono.core.data.repository

import it.stefanobusceti.krono.core.data.database.TaskDatabase
import it.stefanobusceti.krono.core.data.database.TaskEntity
import it.stefanobusceti.krono.core.data.database.toDomain
import it.stefanobusceti.krono.core.data.database.toEntity
import it.stefanobusceti.krono.core.domain.Task
import it.stefanobusceti.krono.core.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class TaskRepositoryImpl(
    private val database: TaskDatabase
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return database.taskDao().getAll().map { it.toDomain() }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun addTask(taskName: String): Result<Task> {
        return try {
            val entity = TaskEntity(
                name = taskName,
                startTime = Clock.System.now().toEpochMilliseconds()
            )
            database.taskDao().insert(entity)
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(task: Task): Result<Boolean> {
        return try {
            database.taskDao().delete(task.toEntity())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAll(): Result<Boolean> {
        return try {
            database.taskDao().deleteAll()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun pause(id: Int, totalTime: Long) {
        database.taskDao().pause(id, totalTime)
    }

    override suspend fun pauseAll() {
        database.taskDao().pauseAll()
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun resume(id: Int, startTime: Long) {
        database.taskDao().resume(id,startTime)
    }

    override suspend fun getTaskByName(name: String): Task? {
        return database.taskDao().getTaskByName(name)?.toDomain()
    }

    override suspend fun getTaskById(id: Int): Task? {
        return database.taskDao().getTaskByID(id)?.toDomain()
    }
}