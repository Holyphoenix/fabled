package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: String,
    val projectId: String,
    val name: String,
    val color: String
)
