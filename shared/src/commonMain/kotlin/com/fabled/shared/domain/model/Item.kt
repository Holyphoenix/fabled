package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val projectId: String,
    val name: String,
    val description: String,
    val significance: String?,
    val createdAt: Long,
    val updatedAt: Long
)
