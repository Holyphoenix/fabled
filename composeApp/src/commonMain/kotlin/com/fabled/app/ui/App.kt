package com.fabled.app.ui

import androidx.compose.runtime.*
import com.fabled.app.ui.theme.FabledTheme
import com.fabled.app.ui.dashboard.DashboardScreen
import com.fabled.app.ui.drafting.DraftingScreen
import com.fabled.app.ui.worldbuilding.WorldBuildingScreen
import com.fabled.app.ui.characters.CharacterScreen
import com.fabled.app.ui.timeline.TimelineScreen
import com.fabled.shared.domain.model.Project

sealed class Screen {
    object Dashboard : Screen()
    data class Drafting(val project: Project) : Screen()
    data class WorldBuilding(val project: Project) : Screen()
    data class Characters(val project: Project) : Screen()
    data class Timeline(val project: Project) : Screen()
}

@Composable
fun App() {
    FabledTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

        when (val screen = currentScreen) {
            is Screen.Dashboard -> DashboardScreen(
                onOpenProject = { project -> currentScreen = Screen.Drafting(project) }
            )
            is Screen.Drafting -> DraftingScreen(
                project = screen.project,
                onNavigateToWorldBuilding = { currentScreen = Screen.WorldBuilding(screen.project) },
                onNavigateToCharacters = { currentScreen = Screen.Characters(screen.project) },
                onNavigateToTimeline = { currentScreen = Screen.Timeline(screen.project) },
                onBack = { currentScreen = Screen.Dashboard }
            )
            is Screen.WorldBuilding -> WorldBuildingScreen(
                project = screen.project,
                onBack = { currentScreen = Screen.Drafting(screen.project) }
            )
            is Screen.Characters -> CharacterScreen(
                project = screen.project,
                onBack = { currentScreen = Screen.Drafting(screen.project) }
            )
            is Screen.Timeline -> TimelineScreen(
                project = screen.project,
                onBack = { currentScreen = Screen.Drafting(screen.project) }
            )
        }
    }
}
