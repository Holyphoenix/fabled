package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: String,
    val projectId: String,
    val name: String,
    val description: String,
    val details: String,
    val createdAt: Long,
    val updatedAt: Long
)
