package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    val id: String,
    val projectId: String,
    val title: String,
    val orderIndex: Int,
    val summary: String?,
    val createdAt: Long,
    val updatedAt: Long
)
