package it.stefanobusceti.krono.feature.main.presentation

import it.stefanobusceti.krono.core.domain.Task

sealed interface MainScreenAction {
    data class OnTextInput(val text: String) : MainScreenAction
    data class OnTextChange(val text: String) : MainScreenAction
    data class DeleteTask(val task: Task) : MainScreenAction
    data object DeleteAllTask : MainScreenAction
    data class ToggleRunning(val id: Int) : MainScreenAction

    data object CloseApp : MainScreenAction

}