package com.fabled.app.viewmodel

import com.fabled.shared.domain.model.Chapter
import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.repository.ProjectRepository
import com.fabled.shared.domain.usecase.chapter.CreateChapterUseCase
import com.fabled.shared.domain.usecase.chapter.DeleteChapterUseCase
import com.fabled.shared.domain.usecase.chapter.GetChaptersUseCase
import com.fabled.shared.domain.usecase.project.UpdateProjectUseCase
import com.fabled.shared.domain.usecase.scene.CreateSceneUseCase
import com.fabled.shared.domain.usecase.scene.GetScenesForChapterUseCase
import com.fabled.shared.domain.usecase.scene.UpdateSceneUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DraftingUiState(
    val project: Project? = null,
    val chapters: List<Chapter> = emptyList(),
    val selectedChapter: Chapter? = null,
    val scenes: List<Scene> = emptyList(),
    val activeScene: Scene? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isZenMode: Boolean = false,
    val wordCount: Int = 0
)

class DraftingViewModel(
    private val getChaptersUseCase: GetChaptersUseCase,
    private val createChapterUseCase: CreateChapterUseCase,
    private val deleteChapterUseCase: DeleteChapterUseCase,
    private val getScenesForChapterUseCase: GetScenesForChapterUseCase,
    private val createSceneUseCase: CreateSceneUseCase,
    private val updateSceneUseCase: UpdateSceneUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val projectRepository: ProjectRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(DraftingUiState())
    val state: StateFlow<DraftingUiState> = _state.asStateFlow()

    private var autoSaveJob: Job? = null
    private var scenesJob: Job? = null

    fun loadProject(project: Project) {
        _state.value = _state.value.copy(project = project, isLoading = true)
        scope.launch {
            getChaptersUseCase(project.id).collect { chapters ->
                _state.value = _state.value.copy(chapters = chapters, isLoading = false)
                // Auto-select first chapter if none selected
                if (_state.value.selectedChapter == null && chapters.isNotEmpty()) {
                    selectChapter(chapters.first())
                }
            }
        }
    }

    fun selectChapter(chapter: Chapter) {
        _state.value = _state.value.copy(selectedChapter = chapter, scenes = emptyList(), activeScene = null)
        scenesJob?.cancel()
        scenesJob = scope.launch {
            getScenesForChapterUseCase(chapter.id).collect { scenes ->
                _state.value = _state.value.copy(scenes = scenes)
                // Auto-select first scene if none
                if (_state.value.activeScene == null && scenes.isNotEmpty()) {
                    selectScene(scenes.first())
                }
            }
        }
    }

    fun createChapter(title: String) {
        val project = _state.value.project ?: return
        scope.launch {
            runCatching {
                val nextIndex = _state.value.chapters.size
                createChapterUseCase(project.id, title, nextIndex)
            }.onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun deleteChapter(chapterId: String) {
        scope.launch {
            runCatching { deleteChapterUseCase(chapterId) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
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
        // Debounced auto-save after 1.5 seconds of inactivity
        autoSaveJob?.cancel()
        autoSaveJob = scope.launch {
            delay(1500L)
            saveScene()
        }
    }

    fun saveScene() {
        val scene = _state.value.activeScene ?: return
        scope.launch {
            _state.value = _state.value.copy(isSaving = true)
            runCatching { updateSceneUseCase(scene) }
                .onSuccess { updated ->
                    _state.value = _state.value.copy(activeScene = updated, isSaving = false)
                    syncProjectWordCount()
                }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message, isSaving = false) }
        }
    }

    fun createScene(title: String) {
        val chapter = _state.value.selectedChapter ?: return
        val project = _state.value.project ?: return
        scope.launch {
            val nextIndex = _state.value.scenes.size
            runCatching { createSceneUseCase(chapter.id, project.id, title, nextIndex) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun toggleZenMode() {
        _state.value = _state.value.copy(isZenMode = !_state.value.isZenMode)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    /** Recalculates the project word count from all scenes and persists it. */
    private fun syncProjectWordCount() {
        val project = _state.value.project ?: return
        scope.launch {
            // Sum word counts of all saved scenes for the project
            val allScenes = _state.value.scenes
            val totalWords = allScenes.sumOf { it.wordCount }
            val updatedProject = project.copy(wordCount = totalWords)
            runCatching { updateProjectUseCase(updatedProject) }
                .onSuccess { _state.value = _state.value.copy(project = it) }
        }
    }
}

