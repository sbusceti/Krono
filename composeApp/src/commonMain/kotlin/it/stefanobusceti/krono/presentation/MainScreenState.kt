package it.stefanobusceti.krono.presentation

import androidx.compose.material3.SnackbarHostState
import it.stefanobusceti.krono.domain.Task

data class MainScreenState(
    val taskInputText: String = "",
    val taskList: List<Task> = emptyList(),
    val snackBarHostState: SnackbarHostState = SnackbarHostState(),
    val dialogState: DialogState = DialogState.None
)

sealed interface DialogState {
    data object None : DialogState

    data class CreateTask(
        val errorText: String? = null
    ) : DialogState

    data class DeleteTask(
        val taskList: List<Task> = emptyList()
    ) : DialogState
}