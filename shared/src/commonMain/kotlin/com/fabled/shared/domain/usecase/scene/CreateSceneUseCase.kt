package com.fabled.shared.domain.usecase.scene

import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.repository.SceneRepository
import com.fabled.shared.util.IdGenerator

class CreateSceneUseCase(private val repository: SceneRepository) {
    suspend operator fun invoke(
        chapterId: String,
        projectId: String,
        title: String,
        orderIndex: Int = 0
    ): Scene {
        require(title.isNotBlank()) { "Scene title cannot be blank" }
        val now = System.currentTimeMillis()
        val scene = Scene(
            id = IdGenerator.generateId(),
            chapterId = chapterId,
            projectId = projectId,
            title = title.trim(),
            content = "",
            summary = null,
            orderIndex = orderIndex,
            wordCount = 0,
            targetWordCount = null,
            pov = null,
            location = null,
            timelinePosition = null,
            emotionalBeat = null,
            tags = emptyList(),
            createdAt = now,
            updatedAt = now
        )
        repository.insertScene(scene)
        return scene
    }
}
