package it.stefanobusceti.tasktracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import it.stefanobusceti.tasktracker.core.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "TaskTracker",
    ) {
        App()
    }
}