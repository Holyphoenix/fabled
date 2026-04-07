package com.fabled.shared.domain.repository

import com.fabled.shared.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface WorldBuildingRepository {
    fun getLocationsByProject(projectId: String): Flow<List<Location>>
    suspend fun insertLocation(location: Location)
    suspend fun deleteLocation(id: String)
}
