package com.fabled.shared.domain.usecase.project

import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.repository.ProjectRepository
import com.fabled.shared.domain.usecase.assistant.AssistantMode
import com.fabled.shared.util.IdGenerator

class CreateProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(
        title: String,
        description: String = "",
        genre: String = "",
        targetWordCount: Int = 80000,
        assistantMode: AssistantMode = AssistantMode.MODERATE
    ): Project {
        require(title.isNotBlank()) { "Project title cannot be blank" }
        val now = System.currentTimeMillis()
        val project = Project(
            id = IdGenerator.generateId(),
            title = title.trim(),
            description = description,
            genre = genre,
            createdAt = now,
            updatedAt = now,
            wordCount = 0,
            targetWordCount = targetWordCount,
            assistantMode = assistantMode,
            isEncrypted = false
        )
        repository.insertProject(project)
        return project
    }
}
