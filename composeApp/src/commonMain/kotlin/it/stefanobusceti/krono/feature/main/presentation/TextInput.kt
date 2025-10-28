package it.stefanobusceti.krono.feature.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TextInput(
    value:String,
    placeholder: String,
    onTextInput: (text: String) -> Unit,
    onValueChanged: (newValue: String) -> Unit,
    modifier: Modifier
) {

    TextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onTextInput(value)
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent
        ),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        trailingIcon = {
            if(value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onValueChanged("")
                    }
                ){
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Reset",
                        tint = Color.Black
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun TextInputPreview() {
    TextInput(
        value = "",
        placeholder = "Placeholder",
        onTextInput = {},
        modifier = Modifier,
        onValueChanged = {}
    )
}
