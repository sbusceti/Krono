package it.stefanobusceti.krono.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InputDialog(
    title: String,
    placeholder: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: (text: String) -> Unit,
    onDismiss: () -> Unit,
    onValueChange: (text: String) -> Unit = {},
    errorText: String? = null
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    onValueChange(it)
                },
                placeholder = { Text(placeholder) },
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester),
                isError = !errorText.isNullOrEmpty(),
                supportingText = {
                    if (!errorText.isNullOrEmpty()) {
                        Text(text = errorText)
                    }
                }
            )
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                enabled = text.isNotEmpty() && errorText.isNullOrEmpty()
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
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
fun InputDialogPreview() {
    InputDialog(
        title = "Aggiungi nuovo task",
        placeholder = "nome",
        confirmButtonText = "Aggiungi",
        dismissButtonText = "Annulla",
        onConfirm = {},
        onDismiss = {}
    )
}