package com.fabled.shared.domain.usecase.assistant

import com.fabled.shared.domain.model.AssistantSuggestion
import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.model.SuggestionSeverity
import com.fabled.shared.domain.model.SuggestionType
import com.fabled.shared.util.IdGenerator

class GenerateSuggestionUseCase {
    operator fun invoke(
        projectId: String,
        scene: Scene,
        mode: AssistantMode
    ): List<AssistantSuggestion> {
        val suggestions = mutableListOf<AssistantSuggestion>()
        val now = System.currentTimeMillis()

        if (scene.pov == null && mode != AssistantMode.SOFT) {
            suggestions.add(
                AssistantSuggestion(
                    id = IdGenerator.generate(),
                    projectId = projectId,
                    sceneId = scene.id,
                    type = SuggestionType.CONTINUITY_ALERT,
                    content = "Scene '${scene.title}' has no POV character assigned.",
                    severity = SuggestionSeverity.WARNING,
                    isResolved = false,
                    createdAt = now
                )
            )
        }

        if (scene.emotionalBeat == null && mode == AssistantMode.BOLD) {
            suggestions.add(
                AssistantSuggestion(
                    id = IdGenerator.generate(),
                    projectId = projectId,
                    sceneId = scene.id,
                    type = SuggestionType.EMOTIONAL_BEAT,
                    content = "Consider defining the emotional beat for '${scene.title}'.",
                    severity = SuggestionSeverity.INFO,
                    isResolved = false,
                    createdAt = now
                )
            )
        }

        if (scene.wordCount > 5000 && mode != AssistantMode.SOFT) {
            suggestions.add(
                AssistantSuggestion(
                    id = IdGenerator.generate(),
                    projectId = projectId,
                    sceneId = scene.id,
                    type = SuggestionType.PACING,
                    content = "Scene '${scene.title}' is quite long (${scene.wordCount} words). Consider splitting.",
                    severity = SuggestionSeverity.INFO,
                    isResolved = false,
                    createdAt = now
                )
            )
        }

        return suggestions
    }
}
