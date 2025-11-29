package it.stefanobusceti.krono.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.stefanobusceti.krono.domain.TaskRepository
import it.stefanobusceti.krono.domain.usecase.AddTaskUseCase
import it.stefanobusceti.krono.domain.usecase.DeleteTaskUseCase
import it.stefanobusceti.krono.domain.usecase.ToggleRunningUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class MainScreenViewModel(
    taskRepository: TaskRepository,
    private val toggleRunningUseCase: ToggleRunningUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private var _saveEvent = MutableSharedFlow<UiEvent>()
    val saveEvent = _saveEvent.asSharedFlow()
    private val _tasks = taskRepository.getAllTasks()

    private var _state = MutableStateFlow(MainScreenState())

    private val counterFlow = flow {
        while (true) {
            emit(Unit)
        }
    }

    @OptIn(ExperimentalTime::class)
    val state = combine(
        _state,
        _tasks,
        counterFlow
    ) { currentState, tasks, _ ->
        val filteredTasks = if (currentState.taskInputText.isBlank()) {
            tasks
        } else {
            tasks.filter { it.name.contains(currentState.taskInputText, ignoreCase = true) }
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

        currentState.copy(taskList = updatedTasks)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenState()
    )

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.OnTextChange -> {
                _state.update { it.copy(taskInputText = action.text) }
            }

            is MainScreenAction.OnCreateTask -> {
                viewModelScope.launch {
                    addTaskUseCase.invoke(action.name).onFailure {
                        // TODO: Handle error, e.g., show a snackbar
                        println(it.message)
                    }.onSuccess {
                        _state.update {
                            it.copy(
                                taskInputText = "",
                                dialogState = DialogState.None
                            )
                        }
                    }
                }
            }

            is MainScreenAction.RequestDeleteTask -> {
                _state.update { it.copy(dialogState = DialogState.DeleteTask(action.tasks)) }
            }

            is MainScreenAction.ToggleRunning -> {
                viewModelScope.launch {
                    toggleRunningUseCase.invoke(action.id)
                }
            }

            is MainScreenAction.RequestDeleteAllTask -> {
                viewModelScope.launch {
                    //_state.update { it.copy(dialogState = DialogState.DeleteTask()) }
                }
            }

            MainScreenAction.CloseApp -> {
                viewModelScope.launch {
                    _saveEvent.emit(UiEvent.SaveInProgress(true))
                    _tasks.first().filter { it.running }.forEach {
                        toggleRunningUseCase.invoke(it.id)
                    }
                    delay(1.seconds)
                    _saveEvent.emit(UiEvent.SaveInProgress(false))
                }
            }

            MainScreenAction.ConfirmDeleteTask -> {
                val dialogState = _state.value.dialogState as? DialogState.DeleteTask
                if (dialogState != null) {
                    viewModelScope.launch {
                        deleteTaskUseCase.invoke(dialogState.taskList).onFailure { }.onSuccess {
                            _state.update { it.copy(dialogState = DialogState.None) }
                        }
                    }
                }
            }

            MainScreenAction.DismissDialog -> {
                _state.update { it.copy(dialogState = DialogState.None) }
            }

            MainScreenAction.OnAddTaskClick -> {
                _state.update { it.copy(dialogState = DialogState.CreateTask()) }
            }

            is MainScreenAction.OnInputNewTaskName -> {
                viewModelScope.launch {
                    _tasks.first().filter { it.name == action.name.trim() }.let { task ->
                        _state.update { currentState ->
                            currentState.copy(
                                dialogState = DialogState.CreateTask(
                                    errorText = if (task.isNotEmpty()) "Task already exists" else null
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}