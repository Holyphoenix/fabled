package com.fabled.shared.domain.repository

import com.fabled.shared.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersByProject(projectId: String): Flow<List<Character>>
    suspend fun getCharacterById(id: String): Character?
    suspend fun insertCharacter(character: Character)
    suspend fun updateCharacter(character: Character)
    suspend fun deleteCharacter(id: String)
}
