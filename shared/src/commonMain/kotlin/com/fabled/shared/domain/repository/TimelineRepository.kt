package com.fabled.shared.domain.repository

import com.fabled.shared.domain.model.TimelineEvent
import kotlinx.coroutines.flow.Flow

interface TimelineRepository {
    fun getEventsByProject(projectId: String): Flow<List<TimelineEvent>>
    suspend fun insertEvent(event: TimelineEvent)
    suspend fun deleteEvent(id: String)
}
