package com.fabled.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Scene(
    val id: String,
    val chapterId: String,
    val projectId: String,
    val title: String,
    val content: String,
    val summary: String?,
    val orderIndex: Int,
    val wordCount: Int,
    val targetWordCount: Int?,
    val pov: String?,
    val location: String?,
    val timelinePosition: String?,
    val emotionalBeat: String?,
    val tags: List<String>,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun calculateWordCount(): Int = content.split(Regex("\\s+")).filter { it.isNotBlank() }.size
}
