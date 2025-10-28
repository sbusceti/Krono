package it.stefanobusceti.krono.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.stefanobusceti.krono.core.domain.Task
import it.stefanobusceti.krono.core.domain.TaskRepository
import it.stefanobusceti.krono.core.domain.usecase.AddTaskUseCase
import it.stefanobusceti.krono.core.domain.usecase.ToggleRunningUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class MainScreenViewModel(
    private val taskRepository: TaskRepository,
    private val toggleRunningUseCase: ToggleRunningUseCase,
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _searchText = MutableStateFlow("")

    private val _tasks = taskRepository.getAllTasks()

    private val counterFlow = flow {
        while (true) {
            emit(Unit)
            delay(1.seconds)
        }
    }

    @OptIn(ExperimentalTime::class)
    val state = combine(
        _tasks,
        _searchText,
        counterFlow
    ) { tasks, text, _ ->
        val filteredTasks = if (text.isBlank()) {
            tasks
        } else {
            tasks.filter { it.name.contains(text, ignoreCase = true) }
        }.sortedBy { it.id }.reversed()

        val updatedTasks = filteredTasks.map { task ->
            if (task.running) {
                task.copy(
                    totalTime = task.totalTime + (Clock.System.now()
                        .toEpochMilliseconds() - task.startTime)
                )
            } else {
                task
            }
        }

        MainScreenState(
            taskList = updatedTasks,
            taskInputText = text
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenState()
    )

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.OnTextChange -> {
                _searchText.value = action.text
            }

            is MainScreenAction.OnTextInput -> {
                viewModelScope.launch {
                    addTaskUseCase.invoke(action.text).onFailure {
                        // TODO: Handle error, e.g., show a snackbar
                        println(it.message)
                    }.onSuccess {
                        _searchText.value = ""
                    }
                }
            }

            is MainScreenAction.DeleteTask -> {
                deleteTask(action.task)
            }

            is MainScreenAction.ToggleRunning -> {
                viewModelScope.launch {
                    toggleRunningUseCase.invoke(action.id)
                }
            }

            is MainScreenAction.DeleteAllTask -> {
                viewModelScope.launch {
                    taskRepository.deleteAll().onFailure {
                        // TODO: Handle error, e.g., show a snackbar
                    }
                }
            }
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task).onFailure {
                // TODO: Handle error, e.g., show a snackbar
            }
        }
    }
}
