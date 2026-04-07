package com.fabled.shared.domain.usecase.location

import com.fabled.shared.domain.model.Location
import com.fabled.shared.domain.repository.WorldBuildingRepository

class UpdateLocationUseCase(private val repository: WorldBuildingRepository) {
    suspend operator fun invoke(location: Location): Location {
        require(location.name.isNotBlank()) { "Location name cannot be blank" }
        val updated = location.copy(updatedAt = System.currentTimeMillis())
        repository.updateLocation(updated)
        return updated
    }
}
