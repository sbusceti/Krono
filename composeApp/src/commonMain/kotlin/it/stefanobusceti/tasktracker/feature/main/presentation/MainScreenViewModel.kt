package it.stefanobusceti.tasktracker.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.stefanobusceti.tasktracker.core.domain.Task
import it.stefanobusceti.tasktracker.core.domain.TaskRepository
import it.stefanobusceti.tasktracker.core.domain.usecase.AddTaskUseCase
import it.stefanobusceti.tasktracker.core.domain.usecase.ToggleRunningUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MainScreenViewModel(
    private val taskRepository: TaskRepository,
    private val toggleRunningUseCase: ToggleRunningUseCase,
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private var _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    private var allTasks: List<Task> = emptyList()

    init {
        //initial data load with flow
        viewModelScope.launch {
            taskRepository.getAllTasks().map { tasks ->
                allTasks = tasks
                MainScreenState(
                    taskList = tasks.sortedBy { task -> task.id }.reversed(),
                    startStopButtonText = if (hasRunningTasks(tasks)) "Pause all" else "Resume all"
                )
            }.stateIn(
                scope = viewModelScope,
                started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
                initialValue = MainScreenState()
            ).collect { _state.value = it }
        }
    }

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.OnTextChange -> {
                _state.update { currentState ->
                    val filteredList = if (action.text.isBlank()) {
                        allTasks
                    } else {
                        allTasks.filter { task ->
                            task.name.contains(action.text, ignoreCase = true)
                        }
                    }

                    currentState.copy(
                        taskInputText = action.text,
                        taskList = filteredList
                    )
                }
            }

            is MainScreenAction.OnTextInput -> {
                viewModelScope.launch {
                    addTaskUseCase.invoke(action.text).onFailure {
                        println(it.message)
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
                    }
                }
            }
        }
    }

    private fun hasRunningTasks(tasks: List<Task>): Boolean {
        return (tasks.isEmpty() || tasks.any { task -> task.running })
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task).onFailure {
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun updateRunningTaskTime() {
        _state.update { currentState ->

            val updatedAllTasks = allTasks.map { task ->
                if (task.running) {
                    val elapsedSessionTime =
                        Clock.System.now().toEpochMilliseconds() - task.startTime

                    task.copy(
                        totalTime = elapsedSessionTime
                    )
                } else {
                    task
                }
            }

            allTasks = updatedAllTasks

            currentState.copy(
                taskList = updatedAllTasks.filter { task ->
                    task.name.contains(state.value.taskInputText, ignoreCase = true)
                }
            )
        }
    }
}
