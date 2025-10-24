package it.stefanobusceti.tasktracker.core.domain.usecase

import it.stefanobusceti.tasktracker.core.domain.TaskRepository

class DeleteAllTasksUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return taskRepository.deleteAll()
    }
}
