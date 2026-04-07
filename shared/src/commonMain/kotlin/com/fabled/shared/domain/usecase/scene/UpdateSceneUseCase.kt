package com.fabled.shared.domain.usecase.scene

import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.repository.SceneRepository

class UpdateSceneUseCase(private val repository: SceneRepository) {
    suspend operator fun invoke(scene: Scene): Scene {
        val wordCount = scene.calculateWordCount()
        val updated = scene.copy(wordCount = wordCount, updatedAt = System.currentTimeMillis())
        repository.updateScene(updated)
        return updated
    }
}
