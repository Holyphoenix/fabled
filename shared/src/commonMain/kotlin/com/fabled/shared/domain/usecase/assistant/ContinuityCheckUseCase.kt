package com.fabled.shared.domain.usecase.assistant

import com.fabled.shared.domain.model.ContinuityFlag
import com.fabled.shared.domain.model.ContinuityFlagType
import com.fabled.shared.domain.model.Scene
import com.fabled.shared.util.IdGenerator

class ContinuityCheckUseCase {
    operator fun invoke(projectId: String, scenes: List<Scene>): List<ContinuityFlag> {
        val flags = mutableListOf<ContinuityFlag>()
        val now = System.currentTimeMillis()

        val scenesByPov = scenes.groupBy { it.pov }
        scenesByPov.forEach { (pov, povScenes) ->
            if (pov != null && povScenes.size > 1) {
                val adjacentConflicts = povScenes.zipWithNext().filter { (a, b) ->
                    a.location != null && b.location != null && a.location != b.location
                }
                if (adjacentConflicts.isNotEmpty()) {
                    flags.add(
                        ContinuityFlag(
                            id = IdGenerator.generate(),
                            projectId = projectId,
                            type = ContinuityFlagType.TRAVEL_CONTRADICTION,
                            description = "POV character '$pov' appears in different locations without travel.",
                            sceneIds = adjacentConflicts.flatMap { listOf(it.first.id, it.second.id) }.distinct(),
                            isResolved = false,
                            createdAt = now
                        )
                    )
                }
            }
        }

        return flags
    }
}
