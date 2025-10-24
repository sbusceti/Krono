package it.stefanobusceti.tasktracker

import androidx.compose.ui.window.ComposeUIViewController
import it.stefanobusceti.tasktracker.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }