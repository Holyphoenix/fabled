package com.fabled.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fabled.data.FabledDatabase
import com.fabled.shared.domain.model.Character
import com.fabled.shared.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class CharacterRepositoryImpl(private val database: FabledDatabase) : CharacterRepository {

    private val stringListSerializer = ListSerializer(String.serializer())

    override fun getCharactersByProject(projectId: String): Flow<List<Character>> =
        database.fabledDatabaseQueries.selectCharactersByProject(projectId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toCharacter() } }

    override suspend fun getCharacterById(id: String): Character? = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.selectCharacterById(id).executeAsOneOrNull()?.toCharacter()
    }

    override suspend fun insertCharacter(character: Character) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.insertCharacter(
            id = character.id,
            project_id = character.projectId,
            name = character.name,
            description = character.description,
            traits = Json.encodeToString(stringListSerializer, character.traits),
            motivations = Json.encodeToString(stringListSerializer, character.motivations),
            secrets = Json.encodeToString(stringListSerializer, character.secrets),
            arc = character.arc,
            first_appearance = character.firstAppearance,
            created_at = character.createdAt,
            updated_at = character.updatedAt
        )
    }

    override suspend fun updateCharacter(character: Character) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.updateCharacter(
            name = character.name,
            description = character.description,
            traits = Json.encodeToString(stringListSerializer, character.traits),
            motivations = Json.encodeToString(stringListSerializer, character.motivations),
            secrets = Json.encodeToString(stringListSerializer, character.secrets),
            arc = character.arc,
            first_appearance = character.firstAppearance,
            updated_at = character.updatedAt,
            id = character.id
        )
    }

    override suspend fun deleteCharacter(id: String) = withContext(Dispatchers.IO) {
        database.fabledDatabaseQueries.deleteCharacter(id)
    }

    private fun com.fabled.data.Character.toCharacter(): Character {
        return Character(
            id = id,
            projectId = project_id,
            name = name,
            description = description,
            traits = runCatching { Json.decodeFromString(stringListSerializer, traits) }.getOrDefault(emptyList()),
            motivations = runCatching { Json.decodeFromString(stringListSerializer, motivations) }.getOrDefault(emptyList()),
            secrets = runCatching { Json.decodeFromString(stringListSerializer, secrets) }.getOrDefault(emptyList()),
            arc = arc,
            relationships = emptyList(),
            firstAppearance = first_appearance,
            createdAt = created_at,
            updatedAt = updated_at
        )
    }
}
