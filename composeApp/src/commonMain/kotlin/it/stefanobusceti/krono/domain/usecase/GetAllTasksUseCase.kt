package it.stefanobusceti.krono.domain.usecase

import it.stefanobusceti.krono.domain.Task
import it.stefanobusceti.krono.domain.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getAllTasks()
    }
}
