package com.fabled.app.viewmodel

import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.usecase.project.CreateProjectUseCase
import com.fabled.shared.domain.usecase.project.DeleteProjectUseCase
import com.fabled.shared.domain.usecase.project.GetProjectsUseCase
import com.fabled.shared.domain.usecase.project.UpdateProjectUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class ProjectUiState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedProject: Project? = null
)

class ProjectViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(ProjectUiState(isLoading = true))
    val state: StateFlow<ProjectUiState> = _state.asStateFlow()

    init {
        scope.launch {
            getProjectsUseCase()
                .catch { e -> _state.value = _state.value.copy(error = e.message, isLoading = false) }
                .collect { projects ->
                    _state.value = _state.value.copy(projects = projects, isLoading = false)
                }
        }
    }

    fun createProject(
        title: String,
        description: String = "",
        genre: String = "",
        targetWordCount: Int = 80000
    ) {
        scope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { createProjectUseCase(title, description, genre, targetWordCount) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message, isLoading = false) }
                .onSuccess { _state.value = _state.value.copy(isLoading = false) }
        }
    }

    fun updateProject(project: Project) {
        scope.launch {
            runCatching { updateProjectUseCase(project) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun selectProject(project: Project) {
        _state.value = _state.value.copy(selectedProject = project)
    }

    fun deleteProject(projectId: String) {
        scope.launch {
            runCatching { deleteProjectUseCase(projectId) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}

