package com.fabled.shared.domain.usecase.scene

import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.repository.SceneRepository
import kotlinx.coroutines.flow.Flow

class GetScenesForChapterUseCase(private val repository: SceneRepository) {
    operator fun invoke(chapterId: String): Flow<List<Scene>> =
        repository.getScenesByChapter(chapterId)
}
