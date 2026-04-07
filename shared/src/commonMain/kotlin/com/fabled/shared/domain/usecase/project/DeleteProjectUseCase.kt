package com.fabled.shared.domain.usecase.project

import com.fabled.shared.domain.repository.ProjectRepository

class DeleteProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(projectId: String) {
        repository.deleteProject(projectId)
    }
}
