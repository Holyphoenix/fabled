package com.fabled.app.viewmodel

import com.fabled.shared.domain.model.Character
import com.fabled.shared.domain.model.Location
import com.fabled.shared.domain.repository.CharacterRepository
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
    val isLoading: Boolean = false,
    val error: String? = null
)

class WorldBuildingViewModel(
    private val characterRepository: CharacterRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(WorldBuildingUiState())
    val state: StateFlow<WorldBuildingUiState> = _state.asStateFlow()

    fun loadForProject(projectId: String) {
        scope.launch {
            characterRepository.getCharactersByProject(projectId)
                .collect { characters ->
                    _state.value = _state.value.copy(characters = characters, isLoading = false)
                }
        }
    }

    fun selectCharacter(character: Character) {
        _state.value = _state.value.copy(selectedCharacter = character)
    }

    fun clearSelection() {
        _state.value = _state.value.copy(selectedCharacter = null)
    }
}
