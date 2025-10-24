package it.stefanobusceti.tasktracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stefanobusceti.tasktracker.feature.main.presentation.MainScreen
import it.stefanobusceti.tasktracker.feature.main.presentation.MainScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val focusRequester = remember { FocusRequester() }
        val mainScreenViewModel = koinViewModel<MainScreenViewModel>()
        val state by mainScreenViewModel.state.collectAsStateWithLifecycle()
        MainScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            focusRequester = focusRequester,
            state = state,
            onAction = mainScreenViewModel::onAction
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}