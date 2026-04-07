package com.fabled.shared.domain.usecase

import com.fabled.shared.domain.model.Scene
import com.fabled.shared.domain.model.SuggestionType
import com.fabled.shared.domain.usecase.assistant.AssistantMode
import com.fabled.shared.domain.usecase.assistant.GenerateSuggestionUseCase
import kotlin.test.Test
import kotlin.test.assertTrue

class GenerateSuggestionUseCaseTest {
    private val useCase = GenerateSuggestionUseCase()

    private fun makeScene(
        pov: String? = null,
        emotionalBeat: String? = null,
        wordCount: Int = 100,
        content: String = "test"
    ) = Scene(
        id = "s1", chapterId = "c1", projectId = "p1",
        title = "Test Scene", content = content,
        summary = null, orderIndex = 0, wordCount = wordCount,
        targetWordCount = null, pov = pov, location = null,
        timelinePosition = null, emotionalBeat = emotionalBeat,
        tags = emptyList(), createdAt = 0L, updatedAt = 0L
    )

    @Test
    fun softModeGeneratesNoPovWarning() {
        val suggestions = useCase("p1", makeScene(pov = null), AssistantMode.SOFT)
        assertTrue(suggestions.none { it.type == SuggestionType.CONTINUITY_ALERT })
    }

    @Test
    fun moderateModeGeneratesPovWarning() {
        val suggestions = useCase("p1", makeScene(pov = null), AssistantMode.MODERATE)
        assertTrue(suggestions.any { it.type == SuggestionType.CONTINUITY_ALERT })
    }

    @Test
    fun boldModeGeneratesEmotionalBeatSuggestion() {
        val suggestions = useCase("p1", makeScene(pov = "Alice", emotionalBeat = null), AssistantMode.BOLD)
        assertTrue(suggestions.any { it.type == SuggestionType.EMOTIONAL_BEAT })
    }

    @Test
    fun longSceneGeneratesPacingSuggestion() {
        val suggestions = useCase("p1", makeScene(pov = "Alice", wordCount = 6000), AssistantMode.MODERATE)
        assertTrue(suggestions.any { it.type == SuggestionType.PACING })
    }
}
