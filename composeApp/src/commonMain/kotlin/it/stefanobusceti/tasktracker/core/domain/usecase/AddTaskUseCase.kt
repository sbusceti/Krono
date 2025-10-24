package it.stefanobusceti.tasktracker.core.domain.usecase

import it.stefanobusceti.tasktracker.core.domain.Task
import it.stefanobusceti.tasktracker.core.domain.TaskRepository

class AddTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskName: String): Result<Task> {
        val task = taskRepository.getTaskByName(taskName)
        if(task != null) return Result.failure(Exception("Task already exists"))
        return taskRepository.addTask(taskName)
    }
}
