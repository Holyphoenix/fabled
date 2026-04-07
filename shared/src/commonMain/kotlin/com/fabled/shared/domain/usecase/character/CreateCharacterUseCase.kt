package com.fabled.shared.domain.usecase.character

import com.fabled.shared.domain.model.Character
import com.fabled.shared.domain.repository.CharacterRepository
import com.fabled.shared.util.IdGenerator

class CreateCharacterUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(
        projectId: String,
        name: String,
        description: String = "",
        traits: List<String> = emptyList(),
        motivations: List<String> = emptyList()
    ): Character {
        require(name.isNotBlank()) { "Character name cannot be blank" }
        val now = System.currentTimeMillis()
        val character = Character(
            id = IdGenerator.generateId(),
            projectId = projectId,
            name = name.trim(),
            description = description,
            traits = traits,
            motivations = motivations,
            secrets = emptyList(),
            arc = null,
            relationships = emptyList(),
            firstAppearance = null,
            createdAt = now,
            updatedAt = now
        )
        repository.insertCharacter(character)
        return character
    }
}
