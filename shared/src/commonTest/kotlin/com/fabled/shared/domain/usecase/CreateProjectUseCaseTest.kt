package com.fabled.shared.domain.usecase

import com.fabled.shared.domain.model.Project
import com.fabled.shared.domain.repository.ProjectRepository
import com.fabled.shared.domain.usecase.assistant.AssistantMode
import com.fabled.shared.domain.usecase.project.CreateProjectUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class CreateProjectUseCaseTest {
    private val fakeRepository = object : ProjectRepository {
        private val projects = mutableListOf<Project>()
        override fun getAllProjects(): Flow<List<Project>> = flowOf(projects)
        override suspend fun getProjectById(id: String): Project? = projects.find { it.id == id }
        override suspend fun insertProject(project: Project) { projects.add(project) }
        override suspend fun updateProject(project: Project) {}
        override suspend fun deleteProject(id: String) { projects.removeAll { it.id == id } }
    }

    private val useCase = CreateProjectUseCase(fakeRepository)

    @Test
    fun createProjectWithValidTitle() = runTest {
        val project = useCase("My Novel")
        assertEquals("My Novel", project.title)
        assertNotNull(project.id)
        assertEquals(0, project.wordCount)
        assertEquals(AssistantMode.MODERATE, project.assistantMode)
    }

    @Test
    fun createProjectWithBlankTitleThrows() = runTest {
        assertFailsWith<IllegalArgumentException> {
            useCase("   ")
        }
    }

    @Test
    fun createProjectTrimsTitle() = runTest {
        val project = useCase("  My Novel  ")
        assertEquals("My Novel", project.title)
    }
}
