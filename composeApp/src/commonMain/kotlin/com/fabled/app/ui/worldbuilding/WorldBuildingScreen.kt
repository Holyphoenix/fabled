package com.fabled.app.ui.worldbuilding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.app.viewmodel.WorldBuildingViewModel
import com.fabled.shared.domain.model.Project
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldBuildingScreen(
    project: Project,
    onBack: () -> Unit,
    viewModel: WorldBuildingViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(project.id) {
        viewModel.loadForProject(project.id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("World Building — ${project.title}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text("Characters", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                if (state.characters.isEmpty()) {
                    Text("No characters yet", style = MaterialTheme.typography.bodyMedium)
                } else {
                    LazyColumn {
                        items(state.characters) { character ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.selectCharacter(character) }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(character.name, style = MaterialTheme.typography.titleSmall)
                                    if (character.description.isNotBlank()) {
                                        Text(
                                            character.description.take(100),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            state.selectedCharacter?.let { character ->
                HorizontalDivider(modifier = Modifier.fillMaxHeight().width(1.dp))
                Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                    Text(character.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("Description", style = MaterialTheme.typography.labelMedium)
                    Text(character.description)
                    Spacer(Modifier.height(8.dp))
                    if (character.traits.isNotEmpty()) {
                        Text("Traits", style = MaterialTheme.typography.labelMedium)
                        Text(character.traits.joinToString(", "))
                    }
                    if (character.motivations.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text("Motivations", style = MaterialTheme.typography.labelMedium)
                        Text(character.motivations.joinToString(", "))
                    }
                }
            }
        }
    }
}
