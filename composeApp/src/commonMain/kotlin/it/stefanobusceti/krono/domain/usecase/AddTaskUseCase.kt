package it.stefanobusceti.krono.domain.usecase

import it.stefanobusceti.krono.domain.Task
import it.stefanobusceti.krono.domain.TaskRepository

class AddTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskName: String): Result<Task> {
        val task = taskRepository.getTaskByName(taskName)
        if(task != null) return Result.failure(Exception("Task already exists"))
        return taskRepository.addTask(taskName)
    }
}
