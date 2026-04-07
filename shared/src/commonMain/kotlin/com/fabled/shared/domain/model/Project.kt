package com.fabled.shared.domain.model

import com.fabled.shared.domain.usecase.assistant.AssistantMode
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val genre: String,
    val createdAt: Long,
    val updatedAt: Long,
    val wordCount: Int,
    val targetWordCount: Int,
    val assistantMode: AssistantMode,
    val isEncrypted: Boolean
)
