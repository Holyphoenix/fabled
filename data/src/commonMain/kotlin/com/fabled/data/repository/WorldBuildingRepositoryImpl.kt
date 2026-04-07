package com.fabled.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fabled.data.FabledDatabase
import com.fabled.shared.domain.model.Location
import com.fabled.shared.domain.repository.WorldBuildingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WorldBuildingRepositoryImpl(private val database: FabledDatabase) : WorldBuildingRepository {

    override fun getLocationsByProject(projectId: String): Flow<List<Location>> =
        database.fabledDatabaseQueries.selectLocationsByProject(projectId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toLocation() } }

    override suspend fun insertLocation(location: Location) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.insertLocation(
            id = location.id,
            project_id = location.projectId,
            name = location.name,
            description = location.description,
            details = location.details,
            created_at = location.createdAt,
            updated_at = location.updatedAt
        )
    }

    override suspend fun deleteLocation(id: String) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.deleteLocation(id)
    }

    private fun com.fabled.data.Location.toLocation() = Location(
        id = id,
        projectId = project_id,
        name = name,
        description = description,
        details = details,
        createdAt = created_at,
        updatedAt = updated_at
    )
}
