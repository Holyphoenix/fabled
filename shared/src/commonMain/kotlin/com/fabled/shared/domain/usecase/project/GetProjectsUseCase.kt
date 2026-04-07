package com.fabled.shared.domain.usecase.project

import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class GetProjectsUseCase(private val repository: ProjectRepository) {
    operator fun invoke(): Flow<List<Project>> = repository.getAllProjects()
}
