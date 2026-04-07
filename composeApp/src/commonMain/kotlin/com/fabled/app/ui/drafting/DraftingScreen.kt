package com.fabled.app.ui.drafting

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.app.ui.assistant.AssistantPanel
import com.fabled.app.ui.components.AdaptiveSidebar
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
    val assistantState by assistantViewModel.state.collectAsState()

    // Load chapters when project opens
    LaunchedEffect(project.id) {
        draftingViewModel.loadProject(project)
    }

    // Re-analyse scene whenever the active scene changes
    LaunchedEffect(state.activeScene?.id) {
        state.activeScene?.let { scene ->
            assistantViewModel.analyzeScene(project.id, scene)
        }
    }

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

    // Show error snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            draftingViewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.project?.title ?: project.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToWorldBuilding) { Text("World") }
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
            // Left: adaptive chapter/scene sidebar
            AdaptiveSidebar(
                project = state.project ?: project,
                modifier = Modifier.fillMaxHeight()
            )

            // Center: writing area
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                val activeScene = state.activeScene
                if (activeScene != null) {
                    WritingArea(
                        content = activeScene.content,
                        wordCount = state.wordCount,
                        isSaving = state.isSaving,
                        onContentChange = draftingViewModel::updateContent,
                        onSave = draftingViewModel::saveScene,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            if (state.chapters.isEmpty()) {
                                Text("Create a chapter to get started", style = MaterialTheme.typography.bodyLarge)
                            } else if (state.scenes.isEmpty()) {
                                Text("Create a scene in this chapter", style = MaterialTheme.typography.bodyLarge)
                            } else {
                                Text("Select a scene to start writing", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }

            // Right: assistant / contextual panel
            VerticalDivider()
            AssistantPanel(
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$wordCount words", style = MaterialTheme.typography.bodySmall)
            if (isSaving) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 1.5.dp)
                    Spacer(Modifier.width(4.dp))
                    Text("Saving…", style = MaterialTheme.typography.bodySmall)
                }
            } else {
                TextButton(onClick = onSave, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Text("Save")
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            modifier = Modifier.fillMaxSize(),
            placeholder = { Text("Start writing your scene…") }
        )
    }
}

