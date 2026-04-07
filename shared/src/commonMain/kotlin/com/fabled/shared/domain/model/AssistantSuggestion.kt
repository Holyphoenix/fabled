package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AssistantSuggestion(
    val id: String,
    val projectId: String,
    val sceneId: String?,
    val type: SuggestionType,
    val content: String,
    val severity: SuggestionSeverity,
    val isResolved: Boolean,
    val createdAt: Long
)

enum class SuggestionType {
    CONTINUITY_ALERT,
    WORLD_BUILDING,
    SUBPLOT_GAP,
    PACING,
    STRUCTURAL,
    INSPIRATION,
    CHARACTER_ARC,
    EMOTIONAL_BEAT
}

enum class SuggestionSeverity {
    INFO, WARNING, CRITICAL
}
