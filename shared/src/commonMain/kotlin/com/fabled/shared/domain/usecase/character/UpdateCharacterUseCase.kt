package com.fabled.shared.domain.usecase.character

import com.fabled.shared.domain.model.Character
import com.fabled.shared.domain.repository.CharacterRepository

class UpdateCharacterUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(character: Character): Character {
        require(character.name.isNotBlank()) { "Character name cannot be blank" }
        val updated = character.copy(updatedAt = System.currentTimeMillis())
        repository.updateCharacter(updated)
        return updated
    }
}
