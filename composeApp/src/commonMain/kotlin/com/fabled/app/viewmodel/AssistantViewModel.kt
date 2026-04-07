package com.fabled.app.viewmodel

import com.fabled.shared.domain.model.AssistantSuggestion
import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.usecase.assistant.AssistantMode
import com.fabled.shared.domain.usecase.assistant.ContinuityCheckUseCase
import com.fabled.shared.domain.usecase.assistant.GenerateSuggestionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AssistantUiState(
    val suggestions: List<AssistantSuggestion> = emptyList(),
    val mode: AssistantMode = AssistantMode.MODERATE,
    val isVisible: Boolean = true
)

class AssistantViewModel(
    private val generateSuggestionUseCase: GenerateSuggestionUseCase,
    private val continuityCheckUseCase: ContinuityCheckUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(AssistantUiState())
    val state: StateFlow<AssistantUiState> = _state.asStateFlow()

    fun analyzeScene(projectId: String, scene: Scene) {
        scope.launch {
            val suggestions = generateSuggestionUseCase(projectId, scene, _state.value.mode)
            _state.value = _state.value.copy(suggestions = suggestions)
        }
    }

    fun runContinuityCheck(projectId: String, scenes: List<Scene>) {
        scope.launch {
            continuityCheckUseCase(projectId, scenes)
        }
    }

    fun setMode(mode: AssistantMode) {
        _state.value = _state.value.copy(mode = mode)
    }

    fun toggleVisibility() {
        _state.value = _state.value.copy(isVisible = !_state.value.isVisible)
    }

    fun resolveSuggestion(id: String) {
        _state.value = _state.value.copy(
            suggestions = _state.value.suggestions.map { if (it.id == id) it.copy(isResolved = true) else it }
                .filter { !it.isResolved }
        )
    }
}
