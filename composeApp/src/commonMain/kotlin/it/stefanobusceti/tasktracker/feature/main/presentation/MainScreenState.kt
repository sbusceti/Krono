package it.stefanobusceti.tasktracker.feature.main.presentation

import androidx.compose.material3.SnackbarHostState
import it.stefanobusceti.tasktracker.core.domain.Task

data class MainScreenState(
    val taskInputText: String = "",
    val taskList: List<Task> = emptyList(),
    val snackBarHostState: SnackbarHostState = SnackbarHostState()
)