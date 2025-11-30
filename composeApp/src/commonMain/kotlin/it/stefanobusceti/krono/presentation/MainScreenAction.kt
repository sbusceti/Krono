package it.stefanobusceti.krono.presentation

import it.stefanobusceti.krono.domain.Task

sealed interface MainScreenAction {
    data class OnCreateTask(val name: String) : MainScreenAction
    data class OnTextChange(val text: String) : MainScreenAction
    data class RequestDeleteTask(val tasks: List<Task>) : MainScreenAction
    data object ConfirmDeleteTask : MainScreenAction
    data object DismissDialog : MainScreenAction
    data object RequestDeleteAllTask : MainScreenAction
    data class ToggleRunning(val id: Int) : MainScreenAction
    data object CloseApp : MainScreenAction

    data object OnAddTaskClick : MainScreenAction
    data class OnInputNewTaskName(val name: String) : MainScreenAction

    data class OnEditClick(val id: Int,val name: String) : MainScreenAction
    data class OnEditTaskConfirm(val id: Int,val name: String) : MainScreenAction

    data class OnRename(val id: Int,val name: String) : MainScreenAction
}