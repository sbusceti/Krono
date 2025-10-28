package it.stefanobusceti.krono.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.stefanobusceti.krono.core.domain.Task

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    var startTime: Long,
    var running: Boolean = true,
    var totalTime: Long = 0
)

fun TaskEntity.toDomain(): Task = Task(id, name, startTime, running, totalTime)

fun List<TaskEntity>.toDomain(): List<Task> = map { it.toDomain() }

fun Task.toEntity() : TaskEntity = TaskEntity(id, name, startTime, running, totalTime)