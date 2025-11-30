package it.stefanobusceti.krono.presentation

import androidx.compose.runtime.Composable

@Composable
fun DialogHandler(
    dialogState: DialogState,
    taskCount : Int,
    onAction: (MainScreenAction) -> Unit,
){
    when (dialogState) {
        is DialogState.CreateTask -> {
            InputDialog(
                title = "Add new task",
                placeholder = "name",
                confirmButtonText = "Add",
                dismissButtonText = "Cancel",
                onConfirm = { onAction(MainScreenAction.OnCreateTask(it)) },
                onDismiss = { onAction(MainScreenAction.DismissDialog) },
                errorText = dialogState.errorText,
                onValueChange = { onAction(MainScreenAction.OnInputNewTaskName(it)) }
            )
        }

        is DialogState.None -> {
            //nothing to show
        }

        is DialogState.DeleteTask -> {
            val dialogTitle = if ((taskCount == dialogState.taskList.size)
                && taskCount > 1
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

        is DialogState.EditTask -> {
            InputDialog(
                title = "Rename task",
                placeholder = dialogState.name,
                confirmButtonText = "Rename",
                dismissButtonText = "Cancel",
                onConfirm = { onAction(MainScreenAction.OnEditTaskConfirm(dialogState.id,it)) },
                onDismiss = { onAction(MainScreenAction.DismissDialog) },
                errorText = dialogState.errorText,
                onValueChange = {onAction(MainScreenAction.OnRename(dialogState.id,it))}
            )
        }
    }

}