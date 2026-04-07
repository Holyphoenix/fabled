package com.fabled.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fabled.data.FabledDatabase
import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.repository.ProjectRepository
import com.fabled.shared.domain.usecase.assistant.AssistantMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProjectRepositoryImpl(private val database: FabledDatabase) : ProjectRepository {

    override fun getAllProjects(): Flow<List<Project>> =
        database.fabledDatabaseQueries.selectAllProjects()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toProject() } }

    override suspend fun getProjectById(id: String): Project? = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.selectProjectById(id).executeAsOneOrNull()?.toProject()
    }

    override suspend fun insertProject(project: Project) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.insertProject(
            id = project.id,
            title = project.title,
            description = project.description,
            genre = project.genre,
            created_at = project.createdAt,
            updated_at = project.updatedAt,
            word_count = project.wordCount.toLong(),
            target_word_count = project.targetWordCount.toLong(),
            assistant_mode = project.assistantMode.name,
            is_encrypted = if (project.isEncrypted) 1L else 0L
        )
    }

    override suspend fun updateProject(project: Project) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.updateProject(
            title = project.title,
            description = project.description,
            genre = project.genre,
            updated_at = project.updatedAt,
            word_count = project.wordCount.toLong(),
            target_word_count = project.targetWordCount.toLong(),
            assistant_mode = project.assistantMode.name,
            id = project.id
        )
    }

    override suspend fun deleteProject(id: String) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.deleteProject(id)
    }

    private fun com.fabled.data.Project.toProject() = Project(
        id = id,
        title = title,
        description = description,
        genre = genre,
        createdAt = created_at,
        updatedAt = updated_at,
        wordCount = word_count.toInt(),
        targetWordCount = target_word_count.toInt(),
        assistantMode = runCatching { AssistantMode.valueOf(assistant_mode) }.getOrDefault(AssistantMode.MODERATE),
        isEncrypted = is_encrypted != 0L
    )
}
