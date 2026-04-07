package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TimelineEvent(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val position: String,
    val sceneId: String?,
    val characters: List<String>,
    val createdAt: Long
)
