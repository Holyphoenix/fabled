package com.fabled.app.ui.drafting

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.app.ui.components.AdaptiveSidebar
import com.fabled.app.ui.components.ContextualInsightPanel
import com.fabled.app.viewmodel.AssistantViewModel
import com.fabled.app.viewmodel.DraftingViewModel
import com.fabled.shared.domain.model.Project
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftingScreen(
    project: Project,
    onNavigateToWorldBuilding: () -> Unit,
    onNavigateToCharacters: () -> Unit,
    onNavigateToTimeline: () -> Unit,
    onBack: () -> Unit,
    draftingViewModel: DraftingViewModel = koinInject(),
    assistantViewModel: AssistantViewModel = koinInject()
) {
    val state by draftingViewModel.state.collectAsState()

    if (state.isZenMode) {
        ZenModeScreen(
            content = state.activeScene?.content ?: "",
            wordCount = state.wordCount,
            onContentChange = draftingViewModel::updateContent,
            onExitZenMode = draftingViewModel::toggleZenMode,
            onSave = draftingViewModel::saveScene
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToWorldBuilding) { Text("World") }
                    TextButton(onClick = onNavigateToCharacters) { Text("Characters") }
                    TextButton(onClick = onNavigateToTimeline) { Text("Timeline") }
                    if (state.activeScene != null) {
                        IconButton(onClick = draftingViewModel::toggleZenMode) {
                            Icon(Icons.Default.Star, contentDescription = "Zen Mode")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            AdaptiveSidebar(
                project = project,
                modifier = Modifier.width(240.dp).fillMaxHeight()
            )
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                if (state.activeScene != null) {
                    WritingArea(
                        content = state.activeScene!!.content,
                        wordCount = state.wordCount,
                        isSaving = state.isSaving,
                        onContentChange = draftingViewModel::updateContent,
                        onSave = draftingViewModel::saveScene,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        Text(
                            "Select or create a scene to start writing",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            ContextualInsightPanel(
                project = project,
                modifier = Modifier.width(280.dp).fillMaxHeight()
            )
        }
    }
}

@Composable
private fun WritingArea(
    content: String,
    wordCount: Int,
    isSaving: Boolean,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$wordCount words", style = MaterialTheme.typography.bodySmall)
            if (isSaving) {
                Text("Saving...", style = MaterialTheme.typography.bodySmall)
            } else {
                TextButton(onClick = onSave) { Text("Save") }
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            modifier = Modifier.fillMaxSize(),
            placeholder = { Text("Start writing your scene...") }
        )
    }
}
