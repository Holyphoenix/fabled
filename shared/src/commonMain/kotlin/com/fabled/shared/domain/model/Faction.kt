package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Faction(
    val id: String,
    val projectId: String,
    val name: String,
    val description: String,
    val goals: List<String>,
    val members: List<String>,
    val createdAt: Long,
    val updatedAt: Long
)
