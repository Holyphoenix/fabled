package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String,
    val projectId: String,
    val name: String,
    val description: String,
    val traits: List<String>,
    val motivations: List<String>,
    val secrets: List<String>,
    val arc: String?,
    val relationships: List<CharacterRelationship>,
    val firstAppearance: String?,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class CharacterRelationship(
    val characterId: String,
    val relationshipType: String,
    val description: String
)
