package com.fabled.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.fabled.app.viewmodel.DraftingViewModel
import com.fabled.shared.domain.model.Project
import org.koin.compose.koinInject

/**
 * An adaptive sidebar that collapses when not hovered and expands on hover.
 * Shows project chapters and scenes when expanded, with create actions.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AdaptiveSidebar(
    project: Project,
    modifier: Modifier = Modifier,
    viewModel: DraftingViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    var isExpanded by remember { mutableStateOf(true) }
    val sidebarWidth by animateDpAsState(targetValue = if (isExpanded) 240.dp else 40.dp)

    var showNewChapterDialog by remember { mutableStateOf(false) }
    var showNewSceneDialog by remember { mutableStateOf(false) }
    var newItemTitle by remember { mutableStateOf("") }

    Surface(
        modifier = modifier
            .width(sidebarWidth)
            .fillMaxHeight()
            .onPointerEvent(PointerEventType.Enter) { isExpanded = true }
            .onPointerEvent(PointerEventType.Exit) { isExpanded = false },
        tonalElevation = 1.dp
    ) {
        AnimatedVisibility(visible = isExpanded, enter = fadeIn(), exit = fadeOut()) {
            Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                // Project title + progress
                Text(
                    project.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = {
                        if (project.targetWordCount > 0)
                            project.wordCount.toFloat() / project.targetWordCount
                        else 0f
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "${project.wordCount} / ${project.targetWordCount} words",
                    style = MaterialTheme.typography.labelSmall
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Chapters header + add button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Chapters", style = MaterialTheme.typography.labelMedium)
                    IconButton(
                        onClick = { showNewChapterDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Add, "New Chapter", modifier = Modifier.size(16.dp))
                    }
                }

                if (state.chapters.isEmpty()) {
                    Text(
                        "No chapters yet",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    state.chapters.forEach { chapter ->
                        val isSelected = state.selectedChapter?.id == chapter.id
                        item(key = chapter.id) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                        else androidx.compose.ui.graphics.Color.Transparent
                                    )
                                    .clickable { viewModel.selectChapter(chapter) }
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    if (isSelected) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    chapter.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                                )
                                IconButton(
                                    onClick = { viewModel.deleteChapter(chapter.id) },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete Chapter",
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        // Show scenes under selected chapter
                        if (isSelected) {
                            items(state.scenes, key = { it.id }) { scene ->
                                val isActiveScene = state.activeScene?.id == scene.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp)
                                        .background(
                                            if (isActiveScene) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                            else androidx.compose.ui.graphics.Color.Transparent
                                        )
                                        .clickable { viewModel.selectScene(scene) }
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "· ${scene.title}",
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            item {
                                TextButton(
                                    onClick = { showNewSceneDialog = true },
                                    modifier = Modifier.padding(start = 12.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                ) {
                                    Icon(Icons.Default.Add, null, modifier = Modifier.size(12.dp))
                                    Spacer(Modifier.width(2.dp))
                                    Text("New Scene", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // New Chapter dialog
    if (showNewChapterDialog) {
        AlertDialog(
            onDismissRequest = { showNewChapterDialog = false; newItemTitle = "" },
            title = { Text("New Chapter") },
            text = {
                OutlinedTextField(
                    value = newItemTitle,
                    onValueChange = { newItemTitle = it },
                    label = { Text("Chapter title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newItemTitle.isNotBlank()) {
                        viewModel.createChapter(newItemTitle)
                        showNewChapterDialog = false
                        newItemTitle = ""
                    }
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showNewChapterDialog = false; newItemTitle = "" }) {
                    Text("Cancel")
                }
            }
        )
    }

    // New Scene dialog
    if (showNewSceneDialog) {
        AlertDialog(
            onDismissRequest = { showNewSceneDialog = false; newItemTitle = "" },
            title = { Text("New Scene") },
            text = {
                OutlinedTextField(
                    value = newItemTitle,
                    onValueChange = { newItemTitle = it },
                    label = { Text("Scene title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newItemTitle.isNotBlank()) {
                        viewModel.createScene(newItemTitle)
                        showNewSceneDialog = false
                        newItemTitle = ""
                    }
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showNewSceneDialog = false; newItemTitle = "" }) {
                    Text("Cancel")
                }
            }
        )
    }
}

