package com.fabled.shared.domain.repository

import com.fabled.shared.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<List<Project>>
    suspend fun getProjectById(id: String): Project?
    suspend fun insertProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(id: String)
}
