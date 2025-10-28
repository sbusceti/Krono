package it.stefanobusceti.krono.feature.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun TaskItem(
    id: String,
    taskName: String,
    duration: String,
    isRunning: Boolean,
    onStartStopClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nome del task
            Text(
                text = taskName,
                fontSize = 16.sp,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Durata
                    Text(
                        text = duration,
                        /*style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Light,
                            color = Color(0xFF6E7B8B)
                        ),*/
                        modifier = Modifier
                    )
                    //indicatore di stato
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(if (isRunning) Color(0xFF4CAF50) else Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pulsante delete
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Elimina",
                            tint = Color.Black
                        )
                    }

                    // Pulsante Play/Pause
                    IconButton(
                        onClick = onStartStopClick,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pausa" else "Avvia",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(
        id = "1",
        taskName = "Task1",
        duration = "00:45",
        isRunning = true,
        onDeleteClick = {},
        onStartStopClick = {}
    )
}