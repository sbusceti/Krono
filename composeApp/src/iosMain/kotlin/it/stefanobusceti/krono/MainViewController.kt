package it.stefanobusceti.krono

import androidx.compose.ui.window.ComposeUIViewController
import it.stefanobusceti.krono.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }