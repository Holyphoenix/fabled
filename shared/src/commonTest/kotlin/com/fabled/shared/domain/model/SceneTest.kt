package com.fabled.shared.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class SceneTest {
    private fun makeScene(content: String) = Scene(
        id = "s1", chapterId = "c1", projectId = "p1",
        title = "Test", content = content,
        summary = null, orderIndex = 0, wordCount = 0,
        targetWordCount = null, pov = null, location = null,
        timelinePosition = null, emotionalBeat = null,
        tags = emptyList(), createdAt = 0L, updatedAt = 0L
    )

    @Test
    fun emptyContentHasZeroWordCount() {
        assertEquals(0, makeScene("").calculateWordCount())
    }

    @Test
    fun singleWordCount() {
        assertEquals(1, makeScene("hello").calculateWordCount())
    }

    @Test
    fun multipleWordsCount() {
        assertEquals(5, makeScene("the quick brown fox jumps").calculateWordCount())
    }

    @Test
    fun extraWhitespaceIgnored() {
        assertEquals(3, makeScene("  one   two  three  ").calculateWordCount())
    }
}
