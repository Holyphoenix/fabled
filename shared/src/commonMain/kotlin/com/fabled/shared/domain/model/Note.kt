package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String,
    val projectId: String,
    val sceneId: String?,
    val content: String,
    val isInline: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
