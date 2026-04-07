package com.fabled.app.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.app.viewmodel.ProjectViewModel
import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.usecase.assistant.AssistantMode
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onOpenProject: (Project) -> Unit,
    viewModel: ProjectViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var newProjectTitle by remember { mutableStateOf("") }
    var newProjectGenre by remember { mutableStateOf("") }
    var newProjectTarget by remember { mutableStateOf("80000") }
    var editingProject by remember { mutableStateOf<Project?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Fabled — Novel Writing Studio") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "New Project")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.projects.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No projects yet", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("Tap + to create your first novel", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                    items(state.projects, key = { it.id }) { project ->
                        ProjectCard(
                            project = project,
                            onClick = {
                                viewModel.selectProject(project)
                                onOpenProject(project)
                            },
                            onDelete = { viewModel.deleteProject(project.id) },
                            onSettings = { editingProject = project }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // Create project dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false; newProjectTitle = ""; newProjectGenre = ""; newProjectTarget = "80000" },
            title = { Text("New Project") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newProjectTitle,
                        onValueChange = { newProjectTitle = it },
                        label = { Text("Title *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newProjectGenre,
                        onValueChange = { newProjectGenre = it },
                        label = { Text("Genre (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newProjectTarget,
                        onValueChange = { newProjectTarget = it },
                        label = { Text("Target word count") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newProjectTitle.isNotBlank()) {
                            viewModel.createProject(
                                newProjectTitle,
                                genre = newProjectGenre,
                                targetWordCount = newProjectTarget.toIntOrNull() ?: 80000
                            )
                            showCreateDialog = false
                            newProjectTitle = ""
                            newProjectGenre = ""
                            newProjectTarget = "80000"
                        }
                    }
                ) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false; newProjectTitle = ""; newProjectGenre = "" }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit project settings dialog
    editingProject?.let { project ->
        ProjectSettingsDialog(
            project = project,
            onSave = { updated ->
                viewModel.updateProject(updated)
                editingProject = null
            },
            onDismiss = { editingProject = null }
        )
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onSettings: () -> Unit
) {
    val progress = if (project.targetWordCount > 0)
        project.wordCount.toFloat() / project.targetWordCount else 0f

    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(project.title, style = MaterialTheme.typography.titleLarge)
                    if (project.genre.isNotBlank()) {
                        Text(project.genre, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                IconButton(onClick = onSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${project.wordCount} / ${project.targetWordCount} words",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Mode: ${project.assistantMode.name.lowercase().replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ProjectSettingsDialog(
    project: Project,
    onSave: (Project) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember(project.id) { mutableStateOf(project.title) }
    var genre by remember(project.id) { mutableStateOf(project.genre) }
    var description by remember(project.id) { mutableStateOf(project.description) }
    var targetWordCount by remember(project.id) { mutableStateOf(project.targetWordCount.toString()) }
    var assistantMode by remember(project.id) { mutableStateOf(project.assistantMode) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Project Settings") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                OutlinedTextField(
                    value = targetWordCount,
                    onValueChange = { targetWordCount = it },
                    label = { Text("Target word count") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Assistant Mode", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistantMode.entries.forEach { mode ->
                        FilterChip(
                            selected = assistantMode == mode,
                            onClick = { assistantMode = mode },
                            label = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            project.copy(
                                title = title,
                                genre = genre,
                                description = description,
                                targetWordCount = targetWordCount.toIntOrNull() ?: project.targetWordCount,
                                assistantMode = assistantMode
                            )
                        )
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

