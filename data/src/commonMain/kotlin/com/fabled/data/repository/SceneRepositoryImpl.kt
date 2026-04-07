package com.fabled.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fabled.data.FabledDatabase
import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.repository.SceneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class SceneRepositoryImpl(private val database: FabledDatabase) : SceneRepository {

    private val stringListSerializer = ListSerializer(String.serializer())

    override fun getScenesByChapter(chapterId: String): Flow<List<Scene>> =
        database.fabledDatabaseQueries.selectScenesByChapter(chapterId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toScene() } }

    override fun getScenesByProject(projectId: String): Flow<List<Scene>> =
        database.fabledDatabaseQueries.selectScenesByProject(projectId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toScene() } }

    override suspend fun getSceneById(id: String): Scene? = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.selectSceneById(id).executeAsOneOrNull()?.toScene()
    }

    override suspend fun insertScene(scene: Scene) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.insertScene(
            id = scene.id,
            chapter_id = scene.chapterId,
            project_id = scene.projectId,
            title = scene.title,
            content = scene.content,
            summary = scene.summary,
            order_index = scene.orderIndex.toLong(),
            word_count = scene.wordCount.toLong(),
            target_word_count = scene.targetWordCount?.toLong(),
            pov = scene.pov,
            location = scene.location,
            timeline_position = scene.timelinePosition,
            emotional_beat = scene.emotionalBeat,
            tags = Json.encodeToString(stringListSerializer, scene.tags),
            created_at = scene.createdAt,
            updated_at = scene.updatedAt
        )
    }

    override suspend fun updateScene(scene: Scene) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.updateScene(
            title = scene.title,
            content = scene.content,
            summary = scene.summary,
            order_index = scene.orderIndex.toLong(),
            word_count = scene.wordCount.toLong(),
            target_word_count = scene.targetWordCount?.toLong(),
            pov = scene.pov,
            location = scene.location,
            timeline_position = scene.timelinePosition,
            emotional_beat = scene.emotionalBeat,
            tags = Json.encodeToString(stringListSerializer, scene.tags),
            updated_at = scene.updatedAt,
            id = scene.id
        )
    }

    override suspend fun deleteScene(id: String) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.deleteScene(id)
    }

    private fun com.fabled.data.Scene.toScene(): Scene {
        val tagList = runCatching { Json.decodeFromString(stringListSerializer, tags) }.getOrDefault(emptyList())
        return Scene(
            id = id,
            chapterId = chapter_id,
            projectId = project_id,
            title = title,
            content = content,
            summary = summary,
            orderIndex = order_index.toInt(),
            wordCount = word_count.toInt(),
            targetWordCount = target_word_count?.toInt(),
            pov = pov,
            location = location,
            timelinePosition = timeline_position,
            emotionalBeat = emotional_beat,
            tags = tagList,
            createdAt = created_at,
            updatedAt = updated_at
        )
    }
}
