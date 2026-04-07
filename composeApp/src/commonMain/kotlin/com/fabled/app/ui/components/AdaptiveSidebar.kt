package com.fabled.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.shared.domain.model.Project

@Composable
fun AdaptiveSidebar(
    project: Project,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(project.title, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        Text("Chapters", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        Text("No chapters yet", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = { if (project.targetWordCount > 0) project.wordCount.toFloat() / project.targetWordCount else 0f },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "${project.wordCount} / ${project.targetWordCount} words",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
