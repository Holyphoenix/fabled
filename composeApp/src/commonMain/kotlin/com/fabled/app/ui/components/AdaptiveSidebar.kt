package com.fabled.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.fabled.shared.domain.model.Project

/**
 * An adaptive sidebar that collapses when not hovered and expands on hover,
 * providing a calm, minimal experience per the "calm surface, deep water" principle.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AdaptiveSidebar(
    project: Project,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val sidebarWidth by animateDpAsState(targetValue = if (isExpanded) 240.dp else 40.dp)

    Surface(
        modifier = modifier
            .width(sidebarWidth)
            .fillMaxHeight()
            .onPointerEvent(PointerEventType.Enter) { isExpanded = true }
            .onPointerEvent(PointerEventType.Exit) { isExpanded = false },
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AnimatedVisibility(visible = isExpanded, enter = fadeIn(), exit = fadeOut()) {
                Column {
                    Text(project.title, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Text("Chapters", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("No chapters yet", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = {
                            if (project.targetWordCount > 0)
                                project.wordCount.toFloat() / project.targetWordCount
                            else 0f
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${project.wordCount} / ${project.targetWordCount} words",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
