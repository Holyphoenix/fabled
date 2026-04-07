package com.fabled.shared.domain.usecase.chapter

import com.fabled.shared.domain.model.Chapter
import com.fabled.shared.domain.repository.ChapterRepository
import com.fabled.shared.util.IdGenerator

class CreateChapterUseCase(private val repository: ChapterRepository) {
    suspend operator fun invoke(
        projectId: String,
        title: String,
        orderIndex: Int = 0
    ): Chapter {
        require(title.isNotBlank()) { "Chapter title cannot be blank" }
        val now = System.currentTimeMillis()
        val chapter = Chapter(
            id = IdGenerator.generateId(),
            projectId = projectId,
            title = title.trim(),
            orderIndex = orderIndex,
            summary = null,
            createdAt = now,
            updatedAt = now
        )
        repository.insertChapter(chapter)
        return chapter
    }
}
