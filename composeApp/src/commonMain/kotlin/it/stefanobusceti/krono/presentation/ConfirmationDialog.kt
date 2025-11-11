package it.stefanobusceti.krono.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(message) },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = dismissButtonText)
            }
        }
    )
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        title = "Eliminare \"CR TOL 71\"",
        message = "Sei sicuro di voler procedere?",
        confirmButtonText = "Elimina",
        dismissButtonText = "Annulla",
        onConfirm = {},
        onDismiss = {}
    )
}