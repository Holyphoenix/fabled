package com.fabled.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fabled.data.FabledDatabase
import com.fabled.shared.domain.model.Chapter
import com.fabled.shared.domain.repository.ChapterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChapterRepositoryImpl(private val database: FabledDatabase) : ChapterRepository {

    override fun getChaptersByProject(projectId: String): Flow<List<Chapter>> =
        database.fabledDatabaseQueries.selectChaptersByProject(projectId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toChapter() } }

    override suspend fun getChapterById(id: String): Chapter? = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.selectChapterById(id).executeAsOneOrNull()?.toChapter()
    }

    override suspend fun insertChapter(chapter: Chapter) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.insertChapter(
            id = chapter.id,
            project_id = chapter.projectId,
            title = chapter.title,
            order_index = chapter.orderIndex.toLong(),
            summary = chapter.summary,
            created_at = chapter.createdAt,
            updated_at = chapter.updatedAt
        )
    }

    override suspend fun updateChapter(chapter: Chapter) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.updateChapter(
            title = chapter.title,
            order_index = chapter.orderIndex.toLong(),
            summary = chapter.summary,
            updated_at = chapter.updatedAt,
            id = chapter.id
        )
    }

    override suspend fun deleteChapter(id: String) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.deleteChapter(id)
    }

    private fun com.fabled.data.Chapter.toChapter() = Chapter(
        id = id,
        projectId = project_id,
        title = title,
        orderIndex = order_index.toInt(),
        summary = summary,
        createdAt = created_at,
        updatedAt = updated_at
    )
}
