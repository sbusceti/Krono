package it.stefanobusceti.krono

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import it.stefanobusceti.krono.core.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Krono",
    ) {
        App()
    }
}