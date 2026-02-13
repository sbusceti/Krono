package it.stefanobusceti.krono.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.stefanobusceti.krono.domain.TaskRepository
import it.stefanobusceti.krono.domain.usecase.AddTaskUseCase
import it.stefanobusceti.krono.domain.usecase.DeleteTaskUseCase
import it.stefanobusceti.krono.domain.usecase.ToggleRunningUseCase
import it.stefanobusceti.krono.domain.usecase.UpdateTaskNameUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class MainScreenViewModel(
    private val taskRepository: TaskRepository,
    private val toggleRunningUseCase: ToggleRunningUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskNameUseCase: UpdateTaskNameUseCase
) : ViewModel() {

    private var _saveEvent = MutableSharedFlow<UiEvent>()
    val saveEvent = _saveEvent.asSharedFlow()
    private val _tasks = taskRepository.getAllTasks()

    private var _state = MutableStateFlow(MainScreenState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val counterFlow = _tasks
        .map { tasks ->
            tasks.any { it.running }
        }
        .distinctUntilChanged()
        .flatMapLatest { hasRunningTask ->
            if (hasRunningTask) {
                flow {
                    while (true) {
                        emit(Unit)
                        delay(1000L)
                    }
                }
            } else {
                flowOf(Unit)
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
                val now = Clock.System.now().toEpochMilliseconds()
                val taskDuration = now - task.startTime
                task.copy(
                    totalTime = task.totalTime + taskDuration
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

            is MainScreenAction.OnRename -> {
                viewModelScope.launch {
                    val oldName = taskRepository.getTaskById(action.id)?.name
                    _tasks.first().filter { it.name == action.name.trim() }.let { task ->
                        _state.update { currentState ->
                            currentState.copy(
                                dialogState = DialogState.EditTask(
                                    errorText = if (task.isNotEmpty()) "Task already exists" else null,
                                    id = action.id,
                                    name = if (task.isNotEmpty()) action.name else oldName ?: ""
                                )
                            )
                        }
                    }
                }
            }

            is MainScreenAction.OnEditClick -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.EditTask(
                            action.id,
                            action.name
                        )
                    )
                }
            }

            is MainScreenAction.OnEditTaskConfirm -> {
                viewModelScope.launch {
                    updateTaskNameUseCase.invoke(action.id, action.name)
                        .onFailure { }
                        .onSuccess {
                            _state.update { it.copy(dialogState = DialogState.None) }
                        }
                }
            }
        }
    }
}