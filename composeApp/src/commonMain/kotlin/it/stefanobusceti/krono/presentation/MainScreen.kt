package it.stefanobusceti.krono.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.stefanobusceti.krono.presentation.LongExt.formatElapsedTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainScreenState,
    focusRequester: FocusRequester,
    onAction: (MainScreenAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(MainScreenAction.OnAddTaskClick) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new task",
                    tint = Color.Black
                )
            }
        }
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(
                placeholder = "Enter task name",
                onTextInput = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                onValueChanged = { onAction(MainScreenAction.OnTextChange(it)) },
                value = state.taskInputText,
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(
                    items = state.taskList,
                    key = { it.id }
                ) { task ->
                    TaskItem(
                        id = task.id.toString(),
                        taskName = task.name,
                        duration = formatElapsedTime(task.totalTime),
                        isRunning = task.running,
                        onStartStopClick = {
                            onAction(MainScreenAction.ToggleRunning(task.id))
                        },
                        onDeleteClick = {
                            onAction(MainScreenAction.RequestDeleteTask(listOf(task)))
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            SnackbarHost(
                hostState = state.snackBarHostState
            )
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.taskList.count()} tasks"
                )
                Spacer(modifier = Modifier.weight(1f))
                /*Button(
                    onClick = { onAction(MainScreenAction.RequestDeleteAllTask) },
                    enabled = state.taskList.isNotEmpty()
                ) {
                    Text(
                        text = "Delete all"
                    )
                }

                 */
            }
        }

    }
    state.taskToDelete?.let {
        val dialogTitle = if ((state.taskList.size == state.taskToDelete.size)
            && state.taskList.size > 1
        ) "Delete all tasks?"
        else "Delete task?"

        ConfirmationDialog(
            title = dialogTitle,
            message = "Are you sure you want to delete?",
            confirmButtonText = "Delete",
            dismissButtonText = "Cancel",
            onConfirm = { onAction(MainScreenAction.ConfirmDeleteTask) },
            onDismiss = { onAction(MainScreenAction.DismissDialog) }
        )
    }

    when (val dialogState = state.dialogState) {
        is DialogState.CreateTask -> {
            InputDialog(
                title = "Add new task",
                placeholder = "name",
                confirmButtonText = "Add",
                dismissButtonText = "Cancel",
                onConfirm = { onAction(MainScreenAction.OnCreateTask(it)) },
                onDismiss = { onAction(MainScreenAction.DismissDialog) },
                errorText = dialogState.errorText,
                onValueChange = {onAction(MainScreenAction.OnInputNewTaskName(it))}
            )
        }

        is DialogState.None -> {
            //nothing to show
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        focusRequester = FocusRequester(),
        state = MainScreenState(),
        onAction = {}
    )
}