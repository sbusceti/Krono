package it.stefanobusceti.krono.core.domain.usecase

import it.stefanobusceti.krono.core.domain.TaskRepository

class DeleteAllTasksUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return taskRepository.deleteAll()
    }
}
