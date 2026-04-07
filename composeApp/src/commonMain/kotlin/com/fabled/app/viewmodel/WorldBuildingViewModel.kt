package com.fabled.app.viewmodel

import com.fabled.shared.domain.model.Character
import com.fabled.shared.domain.model.Location
import com.fabled.shared.domain.repository.CharacterRepository
import com.fabled.shared.domain.repository.WorldBuildingRepository
import com.fabled.shared.domain.usecase.character.CreateCharacterUseCase
import com.fabled.shared.domain.usecase.character.UpdateCharacterUseCase
import com.fabled.shared.domain.usecase.location.CreateLocationUseCase
import com.fabled.shared.domain.usecase.location.UpdateLocationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorldBuildingUiState(
    val characters: List<Character> = emptyList(),
    val locations: List<Location> = emptyList(),
    val selectedCharacter: Character? = null,
    val selectedLocation: Location? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class WorldBuildingViewModel(
    private val characterRepository: CharacterRepository,
    private val worldBuildingRepository: WorldBuildingRepository,
    private val createCharacterUseCase: CreateCharacterUseCase,
    private val updateCharacterUseCase: UpdateCharacterUseCase,
    private val createLocationUseCase: CreateLocationUseCase,
    private val updateLocationUseCase: UpdateLocationUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(WorldBuildingUiState())
    val state: StateFlow<WorldBuildingUiState> = _state.asStateFlow()

    fun loadForProject(projectId: String) {
        scope.launch {
            launch {
                characterRepository.getCharactersByProject(projectId).collect { characters ->
                    _state.value = _state.value.copy(characters = characters)
                }
            }
            launch {
                worldBuildingRepository.getLocationsByProject(projectId).collect { locations ->
                    _state.value = _state.value.copy(locations = locations)
                }
            }
        }
    }

    // --- Character actions ---

    fun createCharacter(projectId: String, name: String, description: String = "") {
        scope.launch {
            runCatching { createCharacterUseCase(projectId, name, description = description) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun saveCharacter(character: Character) {
        scope.launch {
            runCatching { updateCharacterUseCase(character) }
                .onSuccess { updated -> _state.value = _state.value.copy(selectedCharacter = updated) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun deleteCharacter(characterId: String) {
        scope.launch {
            runCatching { characterRepository.deleteCharacter(characterId) }
                .onSuccess {
                    if (_state.value.selectedCharacter?.id == characterId) {
                        _state.value = _state.value.copy(selectedCharacter = null)
                    }
                }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun selectCharacter(character: Character) {
        _state.value = _state.value.copy(selectedCharacter = character, selectedLocation = null)
    }

    fun clearCharacterSelection() {
        _state.value = _state.value.copy(selectedCharacter = null)
    }

    // --- Location actions ---

    fun createLocation(projectId: String, name: String, description: String = "") {
        scope.launch {
            runCatching { createLocationUseCase(projectId, name, description = description) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun saveLocation(location: Location) {
        scope.launch {
            runCatching { updateLocationUseCase(location) }
                .onSuccess { updated -> _state.value = _state.value.copy(selectedLocation = updated) }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun deleteLocation(locationId: String) {
        scope.launch {
            runCatching { worldBuildingRepository.deleteLocation(locationId) }
                .onSuccess {
                    if (_state.value.selectedLocation?.id == locationId) {
                        _state.value = _state.value.copy(selectedLocation = null)
                    }
                }
                .onFailure { e -> _state.value = _state.value.copy(error = e.message) }
        }
    }

    fun selectLocation(location: Location) {
        _state.value = _state.value.copy(selectedLocation = location, selectedCharacter = null)
    }

    fun clearLocationSelection() {
        _state.value = _state.value.copy(selectedLocation = null)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
