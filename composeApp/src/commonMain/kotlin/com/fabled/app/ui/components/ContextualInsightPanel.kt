package com.fabled.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.app.ui.assistant.AssistantPanel
import com.fabled.shared.domain.model.Project

@Composable
fun ContextualInsightPanel(
    project: Project,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        VerticalDivider(modifier = Modifier.fillMaxHeight())
        AssistantPanel(modifier = Modifier.weight(1f).fillMaxHeight())
    }
}
