package it.stefanobusceti.krono.feature.main.presentation

import androidx.compose.material3.SnackbarHostState
import it.stefanobusceti.krono.core.domain.Task

data class MainScreenState(
    val taskInputText: String = "",
    val taskList: List<Task> = emptyList(),
    val snackBarHostState: SnackbarHostState = SnackbarHostState(),
    val taskToDelete: List<Task>? = null
)