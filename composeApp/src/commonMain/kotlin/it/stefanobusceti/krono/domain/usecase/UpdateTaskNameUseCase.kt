package it.stefanobusceti.krono.domain.usecase

import it.stefanobusceti.krono.domain.TaskRepository

class UpdateTaskNameUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun invoke(id: Int, name: String): Result<Unit> = runCatching {
        if (name.isBlank()) {
            throw IllegalArgumentException("Task name cannot be blank")
        }
        taskRepository.updateTaskName(id, name)
    }
}
