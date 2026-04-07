package com.fabled.app.viewmodel

import com.fabled.shared.domain.model.TimelineEvent
import com.fabled.shared.domain.repository.TimelineRepository
import com.fabled.shared.domain.usecase.timeline.CreateTimelineEventUseCase
import com.fabled.shared.domain.usecase.timeline.GetTimelineEventsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TimelineUiState(
    val events: List<TimelineEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TimelineViewModel(
    private val getTimelineEventsUseCase: GetTimelineEventsUseCase,
    private val createTimelineEventUseCase: CreateTimelineEventUseCase,
    private val timelineRepository: TimelineRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(TimelineUiState())
    val state: StateFlow<TimelineUiState> = _state.asStateFlow()

    fun loadForProject(projectId: String) {
        _state.value = _state.value.copy(isLoading = true)
        scope.launch {
            getTimelineEventsUseCase(projectId).collect { events ->
                _state.value = _state.value.copy(events = events, isLoading = false)
            }
        }
    }

    fun createEvent(
        projectId: String,
        title: String,
        description: String = "",
        position: String = ""
    ) {
        scope.launch {
            runCatching { createTimelineEventUseCase(projectId, title, description, position) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun deleteEvent(eventId: String) {
        scope.launch {
            runCatching { timelineRepository.deleteEvent(eventId) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
