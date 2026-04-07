package com.fabled.app.viewmodel

import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.usecase.scene.CreateSceneUseCase
import com.fabled.shared.domain.usecase.scene.GetScenesForChapterUseCase
import com.fabled.shared.domain.usecase.scene.UpdateSceneUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DraftingUiState(
    val scenes: List<Scene> = emptyList(),
    val activeScene: Scene? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isZenMode: Boolean = false,
    val wordCount: Int = 0
)

class DraftingViewModel(
    private val getScenesForChapterUseCase: GetScenesForChapterUseCase,
    private val createSceneUseCase: CreateSceneUseCase,
    private val updateSceneUseCase: UpdateSceneUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(DraftingUiState())
    val state: StateFlow<DraftingUiState> = _state.asStateFlow()

    fun loadChapter(chapterId: String) {
        scope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getScenesForChapterUseCase(chapterId)
                .collect { scenes ->
                    _state.value = _state.value.copy(scenes = scenes, isLoading = false)
                }
        }
    }

    fun selectScene(scene: Scene) {
        _state.value = _state.value.copy(activeScene = scene, wordCount = scene.wordCount)
    }

    fun updateContent(content: String) {
        val activeScene = _state.value.activeScene ?: return
        val wordCount = activeScene.copy(content = content).calculateWordCount()
        _state.value = _state.value.copy(
            activeScene = activeScene.copy(content = content),
            wordCount = wordCount
        )
    }

    fun saveScene() {
        val scene = _state.value.activeScene ?: return
        scope.launch {
            _state.value = _state.value.copy(isSaving = true)
            runCatching { updateSceneUseCase(scene) }
                .onSuccess { updated -> _state.value = _state.value.copy(activeScene = updated, isSaving = false) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message, isSaving = false) }
        }
    }

    fun toggleZenMode() {
        _state.value = _state.value.copy(isZenMode = !_state.value.isZenMode)
    }

    fun createScene(chapterId: String, projectId: String, title: String) {
        scope.launch {
            runCatching { createSceneUseCase(chapterId, projectId, title) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }
}
