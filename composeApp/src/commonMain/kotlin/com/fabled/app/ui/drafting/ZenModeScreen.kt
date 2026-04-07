package com.fabled.app.ui.drafting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ZenModeScreen(
    content: String,
    wordCount: Int,
    onContentChange: (String) -> Unit,
    onExitZenMode: () -> Unit,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight()
                .align(Alignment.Center)
                .padding(vertical = 48.dp)
        ) {
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFFE8E8E8),
                    unfocusedTextColor = Color(0xFFE8E8E8),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, lineHeight = 32.sp),
                placeholder = { Text("Write freely...", color = Color(0xFF555555)) }
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$wordCount words", color = Color(0xFF666666), fontSize = 12.sp)
                Row {
                    TextButton(onClick = onSave) { Text("Save", color = Color(0xFF888888)) }
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = onExitZenMode) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Zen Mode", tint = Color(0xFF888888))
                    }
                }
            }
        }
    }
}
