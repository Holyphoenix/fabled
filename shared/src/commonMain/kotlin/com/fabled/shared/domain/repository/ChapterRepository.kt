package com.fabled.shared.domain.repository

import com.fabled.shared.domain.model.Chapter
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {
    fun getChaptersByProject(projectId: String): Flow<List<Chapter>>
    suspend fun getChapterById(id: String): Chapter?
    suspend fun insertChapter(chapter: Chapter)
    suspend fun updateChapter(chapter: Chapter)
    suspend fun deleteChapter(id: String)
}
