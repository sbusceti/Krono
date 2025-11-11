package it.stefanobusceti.krono.domain.usecase

import it.stefanobusceti.krono.domain.Task
import it.stefanobusceti.krono.domain.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(tasks: List<Task>): Result<Boolean> {
        return taskRepository.deleteTasks(tasks)
    }
}
