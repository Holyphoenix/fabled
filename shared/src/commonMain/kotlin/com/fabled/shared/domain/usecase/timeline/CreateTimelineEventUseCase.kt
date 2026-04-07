package com.fabled.shared.domain.usecase.timeline

import com.fabled.shared.domain.model.TimelineEvent
import com.fabled.shared.domain.repository.TimelineRepository
import com.fabled.shared.util.IdGenerator

class CreateTimelineEventUseCase(private val repository: TimelineRepository) {
    suspend operator fun invoke(
        projectId: String,
        title: String,
        description: String = "",
        position: String = "",
        sceneId: String? = null,
        characters: List<String> = emptyList()
    ): TimelineEvent {
        require(title.isNotBlank()) { "Event title cannot be blank" }
        val now = System.currentTimeMillis()
        val event = TimelineEvent(
            id = IdGenerator.generateId(),
            projectId = projectId,
            title = title.trim(),
            description = description,
            position = position,
            sceneId = sceneId,
            characters = characters,
            createdAt = now
        )
        repository.insertEvent(event)
        return event
    }
}
