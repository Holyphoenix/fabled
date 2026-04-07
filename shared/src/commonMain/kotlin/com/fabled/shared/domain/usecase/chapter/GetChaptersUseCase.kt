package com.fabled.shared.domain.usecase.chapter

import com.fabled.shared.domain.model.Chapter
import com.fabled.shared.domain.repository.ChapterRepository
import kotlinx.coroutines.flow.Flow

class GetChaptersUseCase(private val repository: ChapterRepository) {
    operator fun invoke(projectId: String): Flow<List<Chapter>> =
        repository.getChaptersByProject(projectId)
}
