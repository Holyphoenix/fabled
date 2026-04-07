package com.fabled.app.ui.characters

import com.fabled.app.ui.worldbuilding.WorldBuildingScreen
import androidx.compose.runtime.Composable
import com.fabled.shared.domain.model.Project

/**
 * CharacterScreen delegates to the WorldBuildingScreen with the Characters tab.
 * Full character management (create, edit, delete) is available there.
 */
@Composable
fun CharacterScreen(
    project: Project,
    onBack: () -> Unit
) {
    WorldBuildingScreen(project = project, onBack = onBack)
}

