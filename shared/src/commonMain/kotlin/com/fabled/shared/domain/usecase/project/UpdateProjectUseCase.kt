package com.fabled.shared.domain.usecase.project

import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.repository.ProjectRepository

class UpdateProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(project: Project): Project {
        require(project.title.isNotBlank()) { "Project title cannot be blank" }
        val updated = project.copy(updatedAt = System.currentTimeMillis())
        repository.updateProject(updated)
        return updated
    }
}
