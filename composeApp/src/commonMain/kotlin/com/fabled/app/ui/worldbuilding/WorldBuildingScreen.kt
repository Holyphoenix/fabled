package com.fabled.app.ui.worldbuilding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabled.app.viewmodel.WorldBuildingViewModel
import com.fabled.shared.domain.model.Character
import com.fabled.shared.domain.model.Location
import com.fabled.shared.domain.model.Project
import org.koin.compose.koinInject

private enum class WorldTab { CHARACTERS, LOCATIONS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldBuildingScreen(
    project: Project,
    onBack: () -> Unit,
    viewModel: WorldBuildingViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(WorldTab.CHARACTERS) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var createDialogName by remember { mutableStateOf("") }
    var createDialogDesc by remember { mutableStateOf("") }

    LaunchedEffect(project.id) {
        viewModel.loadForProject(project.id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("World — ${project.title}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Left panel: list
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                TabRow(selectedTabIndex = selectedTab.ordinal) {
                    Tab(
                        selected = selectedTab == WorldTab.CHARACTERS,
                        onClick = { selectedTab = WorldTab.CHARACTERS; viewModel.clearCharacterSelection() },
                        text = { Text("Characters (${state.characters.size})") }
                    )
                    Tab(
                        selected = selectedTab == WorldTab.LOCATIONS,
                        onClick = { selectedTab = WorldTab.LOCATIONS; viewModel.clearLocationSelection() },
                        text = { Text("Locations (${state.locations.size})") }
                    )
                }

                when (selectedTab) {
                    WorldTab.CHARACTERS -> CharacterList(
                        characters = state.characters,
                        selectedId = state.selectedCharacter?.id,
                        onSelect = viewModel::selectCharacter,
                        onDelete = viewModel::deleteCharacter
                    )
                    WorldTab.LOCATIONS -> LocationList(
                        locations = state.locations,
                        selectedId = state.selectedLocation?.id,
                        onSelect = viewModel::selectLocation,
                        onDelete = viewModel::deleteLocation
                    )
                }
            }

            // Right panel: detail editor
            val hasDetail = state.selectedCharacter != null || state.selectedLocation != null
            if (hasDetail) {
                VerticalDivider()
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    when {
                        state.selectedCharacter != null -> CharacterEditor(
                            character = state.selectedCharacter!!,
                            onSave = viewModel::saveCharacter,
                            onClose = viewModel::clearCharacterSelection
                        )
                        state.selectedLocation != null -> LocationEditor(
                            location = state.selectedLocation!!,
                            onSave = viewModel::saveLocation,
                            onClose = viewModel::clearLocationSelection
                        )
                    }
                }
            }
        }
    }

    // Create dialog
    if (showCreateDialog) {
        val label = if (selectedTab == WorldTab.CHARACTERS) "Character" else "Location"
        AlertDialog(
            onDismissRequest = { showCreateDialog = false; createDialogName = ""; createDialogDesc = "" },
            title = { Text("New $label") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = createDialogName,
                        onValueChange = { createDialogName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = createDialogDesc,
                        onValueChange = { createDialogDesc = it },
                        label = { Text("Description (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (createDialogName.isNotBlank()) {
                        if (selectedTab == WorldTab.CHARACTERS) {
                            viewModel.createCharacter(project.id, createDialogName, createDialogDesc)
                        } else {
                            viewModel.createLocation(project.id, createDialogName, createDialogDesc)
                        }
                        showCreateDialog = false
                        createDialogName = ""
                        createDialogDesc = ""
                    }
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false; createDialogName = ""; createDialogDesc = "" }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CharacterList(
    characters: List<Character>,
    selectedId: String?,
    onSelect: (Character) -> Unit,
    onDelete: (String) -> Unit
) {
    if (characters.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No characters yet", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }
    LazyColumn(contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(characters, key = { it.id }) { character ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(character) },
                colors = CardDefaults.cardColors(
                    containerColor = if (character.id == selectedId)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(character.name, style = MaterialTheme.typography.titleSmall)
                        if (character.description.isNotBlank()) {
                            Text(
                                character.description.take(80),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    IconButton(onClick = { onDelete(character.id) }) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationList(
    locations: List<Location>,
    selectedId: String?,
    onSelect: (Location) -> Unit,
    onDelete: (String) -> Unit
) {
    if (locations.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No locations yet", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }
    LazyColumn(contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(locations, key = { it.id }) { location ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(location) },
                colors = CardDefaults.cardColors(
                    containerColor = if (location.id == selectedId)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(location.name, style = MaterialTheme.typography.titleSmall)
                        if (location.description.isNotBlank()) {
                            Text(
                                location.description.take(80),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    IconButton(onClick = { onDelete(location.id) }) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterEditor(
    character: Character,
    onSave: (Character) -> Unit,
    onClose: () -> Unit
) {
    var name by remember(character.id) { mutableStateOf(character.name) }
    var description by remember(character.id) { mutableStateOf(character.description) }
    var traits by remember(character.id) { mutableStateOf(character.traits.joinToString(", ")) }
    var motivations by remember(character.id) { mutableStateOf(character.motivations.joinToString(", ")) }
    var arc by remember(character.id) { mutableStateOf(character.arc ?: "") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Character Sheet", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = onClose) { Text("Close") }
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
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
            value = traits,
            onValueChange = { traits = it },
            label = { Text("Traits (comma-separated)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = motivations,
            onValueChange = { motivations = it },
            label = { Text("Motivations (comma-separated)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = arc,
            onValueChange = { arc = it },
            label = { Text("Character arc") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        Button(
            onClick = {
                onSave(
                    character.copy(
                        name = name,
                        description = description,
                        traits = traits.split(",").map { it.trim() }.filter { it.isNotBlank() },
                        motivations = motivations.split(",").map { it.trim() }.filter { it.isNotBlank() },
                        arc = arc.ifBlank { null }
                    )
                )
            },
            modifier = Modifier.align(Alignment.End),
            enabled = name.isNotBlank()
        ) { Text("Save") }
    }
}

@Composable
private fun LocationEditor(
    location: Location,
    onSave: (Location) -> Unit,
    onClose: () -> Unit
) {
    var name by remember(location.id) { mutableStateOf(location.name) }
    var description by remember(location.id) { mutableStateOf(location.description) }
    var details by remember(location.id) { mutableStateOf(location.details) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Location Details", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = onClose) { Text("Close") }
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
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
            value = details,
            onValueChange = { details = it },
            label = { Text("Additional details") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Button(
            onClick = { onSave(location.copy(name = name, description = description, details = details)) },
            modifier = Modifier.align(Alignment.End),
            enabled = name.isNotBlank()
        ) { Text("Save") }
    }
}

