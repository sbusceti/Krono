package it.stefanobusceti.tasktracker.core.domain.usecase

import it.stefanobusceti.tasktracker.core.domain.TaskRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ToggleRunningUseCase(
    private val taskRepository: TaskRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(id: Int) {
        val task = taskRepository.getTaskById(id)
        if (task == null) throw Exception("Task not found")
        if (task.running) {
            val totalTime = task.totalTime + (Clock.System.now().toEpochMilliseconds() - task.startTime)
            taskRepository.pause(id, totalTime)
        } else {
            taskRepository.resume(id, Clock.System.now().toEpochMilliseconds())
        }
    }
}
