package com.fabled.shared.assistant

import com.fabled.shared.domain.usecase.assistant.AssistantMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AssistantModeTest {
    @Test
    fun allModesExist() {
        assertNotNull(AssistantMode.SOFT)
        assertNotNull(AssistantMode.MODERATE)
        assertNotNull(AssistantMode.BOLD)
    }

    @Test
    fun modesHaveCorrectOrdinals() {
        assertEquals(0, AssistantMode.SOFT.ordinal)
        assertEquals(1, AssistantMode.MODERATE.ordinal)
        assertEquals(2, AssistantMode.BOLD.ordinal)
    }

    @Test
    fun modeFromString() {
        assertEquals(AssistantMode.SOFT, AssistantMode.valueOf("SOFT"))
        assertEquals(AssistantMode.MODERATE, AssistantMode.valueOf("MODERATE"))
        assertEquals(AssistantMode.BOLD, AssistantMode.valueOf("BOLD"))
    }
}
