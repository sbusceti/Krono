package it.stefanobusceti.tasktracker.core.domain.usecase

import it.stefanobusceti.tasktracker.core.domain.Task
import it.stefanobusceti.tasktracker.core.domain.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Result<Boolean> {
        return taskRepository.deleteTask(task)
    }
}
