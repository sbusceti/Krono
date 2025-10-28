package it.stefanobusceti.krono.core.domain.usecase

import it.stefanobusceti.krono.core.domain.Task
import it.stefanobusceti.krono.core.domain.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Result<Boolean> {
        return taskRepository.deleteTask(task)
    }
}
