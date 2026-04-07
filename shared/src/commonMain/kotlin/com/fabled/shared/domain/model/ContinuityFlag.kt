package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ContinuityFlag(
    val id: String,
    val projectId: String,
    val type: ContinuityFlagType,
    val description: String,
    val sceneIds: List<String>,
    val isResolved: Boolean,
    val createdAt: Long
)

enum class ContinuityFlagType {
    AGE_INCONSISTENCY,
    TRAVEL_CONTRADICTION,
    TIMELINE_OVERLAP,
    CHARACTER_DISAPPEARANCE,
    UNRESOLVED_SETUP,
    POV_INCONSISTENCY
}
