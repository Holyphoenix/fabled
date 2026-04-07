package com.fabled.shared.domain.usecase.location

import com.fabled.shared.domain.model.Location
import com.fabled.shared.domain.repository.WorldBuildingRepository
import com.fabled.shared.util.IdGenerator

class CreateLocationUseCase(private val repository: WorldBuildingRepository) {
    suspend operator fun invoke(
        projectId: String,
        name: String,
        description: String = "",
        details: String = ""
    ): Location {
        require(name.isNotBlank()) { "Location name cannot be blank" }
        val now = System.currentTimeMillis()
        val location = Location(
            id = IdGenerator.generateId(),
            projectId = projectId,
            name = name.trim(),
            description = description,
            details = details,
            createdAt = now,
            updatedAt = now
        )
        repository.insertLocation(location)
        return location
    }
}
