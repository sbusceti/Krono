package it.stefanobusceti.krono.core.domain.usecase

import it.stefanobusceti.krono.core.domain.Task
import it.stefanobusceti.krono.core.domain.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getAllTasks()
    }
}
