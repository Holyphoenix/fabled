package com.fabled.shared.domain.usecase.timeline

import com.fabled.shared.domain.model.TimelineEvent
import com.fabled.shared.domain.repository.TimelineRepository
import kotlinx.coroutines.flow.Flow

class GetTimelineEventsUseCase(private val repository: TimelineRepository) {
    operator fun invoke(projectId: String): Flow<List<TimelineEvent>> =
        repository.getEventsByProject(projectId)
}
