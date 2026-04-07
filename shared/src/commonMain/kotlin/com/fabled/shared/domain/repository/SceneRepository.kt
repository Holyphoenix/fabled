package com.fabled.shared.domain.repository

import com.fabled.shared.domain.model.Scene
import kotlinx.coroutines.flow.Flow

interface SceneRepository {
    fun getScenesByChapter(chapterId: String): Flow<List<Scene>>
    fun getScenesByProject(projectId: String): Flow<List<Scene>>
    suspend fun getSceneById(id: String): Scene?
    suspend fun insertScene(scene: Scene)
    suspend fun updateScene(scene: Scene)
    suspend fun deleteScene(id: String)
}
