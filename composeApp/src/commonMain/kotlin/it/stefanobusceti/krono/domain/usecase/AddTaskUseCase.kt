package it.stefanobusceti.krono.domain.usecase

import it.stefanobusceti.krono.domain.Task
import it.stefanobusceti.krono.domain.TaskRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AddTaskUseCase(
    private val taskRepository: TaskRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(taskName: String): Result<Task> {
        val task = taskRepository.getTaskByName(taskName)
        if (task != null) return Result.failure(Exception("Task already exists"))

        val runningTask = taskRepository.getRunningTask()
        runningTask.forEach { runningTask ->
            val totalTime = runningTask.totalTime + (Clock.System.now()
                .toEpochMilliseconds() - runningTask.startTime)
            taskRepository.pause(runningTask.id, totalTime)
        }
        return taskRepository.addTask(taskName)
    }
}
