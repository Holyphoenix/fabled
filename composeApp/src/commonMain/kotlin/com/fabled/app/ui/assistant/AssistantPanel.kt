package com.fabled.app.ui.assistant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.app.viewmodel.AssistantViewModel
import com.fabled.shared.domain.model.AssistantSuggestion
import com.fabled.shared.domain.model.SuggestionSeverity
import com.fabled.shared.domain.usecase.assistant.AssistantMode
import org.koin.compose.koinInject

@Composable
fun AssistantPanel(
    modifier: Modifier = Modifier,
    viewModel: AssistantViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = modifier.padding(8.dp)) {
        Text("Assistant", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            AssistantMode.entries.forEach { mode ->
                FilterChip(
                    selected = state.mode == mode,
                    onClick = { viewModel.setMode(mode) },
                    label = { Text(mode.name) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (state.suggestions.isEmpty()) {
            Text("No suggestions", style = MaterialTheme.typography.bodySmall)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(state.suggestions) { suggestion ->
                    SuggestionCard(
                        suggestion = suggestion,
                        onResolve = { viewModel.resolveSuggestion(suggestion.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionCard(
    suggestion: AssistantSuggestion,
    onResolve: () -> Unit
) {
    val containerColor = when (suggestion.severity) {
        SuggestionSeverity.CRITICAL -> MaterialTheme.colorScheme.errorContainer
        SuggestionSeverity.WARNING -> MaterialTheme.colorScheme.tertiaryContainer
        SuggestionSeverity.INFO -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(colors = CardDefaults.cardColors(containerColor = containerColor)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(suggestion.type.name, style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(2.dp))
            Text(suggestion.content, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = onResolve, contentPadding = PaddingValues(0.dp)) {
                Text("Dismiss", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
