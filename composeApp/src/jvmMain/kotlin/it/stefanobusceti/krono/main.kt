package it.stefanobusceti.krono

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import it.stefanobusceti.krono.di.initKoin
import it.stefanobusceti.krono.presentation.MainScreenAction
import it.stefanobusceti.krono.presentation.MainScreenViewModel
import it.stefanobusceti.krono.presentation.UiEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.java.KoinJavaComponent.get
import java.awt.Desktop

fun main() = application {
    initKoin()
    val mainScreenViewModel = get<MainScreenViewModel>(MainScreenViewModel::class.java)
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
                desktop.setQuitHandler { event, response ->
                    mainScreenViewModel.onAction(MainScreenAction.CloseApp)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        mainScreenViewModel.saveEvent.collectLatest { event ->
            when (event) {
                is UiEvent.SaveInProgress -> {
                    if (event.saveInProgress) {
                        showDialog = true
                    } else {
                        exitApplication()
                    }
                }
            }
        }
    }

    Window(
        onCloseRequest = {
            mainScreenViewModel.onAction(MainScreenAction.CloseApp)
        },
        title = "Krono",
    ) {
        App()

        if (showDialog) {
            Dialog(onDismissRequest = {  }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Save in progress.."
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
