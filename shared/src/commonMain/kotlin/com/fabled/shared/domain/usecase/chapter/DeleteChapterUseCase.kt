package com.fabled.shared.domain.usecase.chapter

import com.fabled.shared.domain.repository.ChapterRepository

class DeleteChapterUseCase(private val repository: ChapterRepository) {
    suspend operator fun invoke(chapterId: String) = repository.deleteChapter(chapterId)
}
