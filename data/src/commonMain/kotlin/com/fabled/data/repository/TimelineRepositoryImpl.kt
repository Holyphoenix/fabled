package com.fabled.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fabled.data.FabledDatabase
import com.fabled.shared.domain.model.TimelineEvent
import com.fabled.shared.domain.repository.TimelineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class TimelineRepositoryImpl(private val database: FabledDatabase) : TimelineRepository {

    private val stringListSerializer = ListSerializer(String.serializer())

    override fun getEventsByProject(projectId: String): Flow<List<TimelineEvent>> =
        database.fabledDatabaseQueries.selectTimelineEventsByProject(projectId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toTimelineEvent() } }

    override suspend fun insertEvent(event: TimelineEvent) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.insertTimelineEvent(
            id = event.id,
            project_id = event.projectId,
            title = event.title,
            description = event.description,
            position = event.position,
            scene_id = event.sceneId,
            characters = Json.encodeToString(stringListSerializer, event.characters),
            created_at = event.createdAt
        )
    }

    override suspend fun deleteEvent(id: String) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.deleteTimelineEvent(id)
    }

    private fun com.fabled.data.TimelineEvent.toTimelineEvent(): TimelineEvent {
        val characterList = runCatching {
            Json.decodeFromString(stringListSerializer, characters)
        }.getOrDefault(emptyList())
        return TimelineEvent(
            id = id,
            projectId = project_id,
            title = title,
            description = description,
            position = position,
            sceneId = scene_id,
            characters = characterList,
            createdAt = created_at
        )
    }
}
