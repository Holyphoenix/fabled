package com.fabled.app.di

import com.fabled.data.database.createFabledDatabase
import com.fabled.data.repository.CharacterRepositoryImpl
import com.fabled.data.repository.ProjectRepositoryImpl
import com.fabled.data.repository.SceneRepositoryImpl
import com.fabled.data.repository.WorldBuildingRepositoryImpl
import com.fabled.shared.domain.usecase.assistant.ContinuityCheckUseCase
import com.fabled.shared.domain.usecase.assistant.GenerateSuggestionUseCase
import com.fabled.shared.domain.usecase.project.CreateProjectUseCase
import com.fabled.shared.domain.usecase.project.DeleteProjectUseCase
import com.fabled.shared.domain.usecase.project.GetProjectsUseCase
import com.fabled.shared.domain.usecase.scene.CreateSceneUseCase
import com.fabled.shared.domain.usecase.scene.GetScenesForChapterUseCase
import com.fabled.shared.domain.usecase.scene.UpdateSceneUseCase
import com.fabled.app.viewmodel.AssistantViewModel
import com.fabled.app.viewmodel.DraftingViewModel
import com.fabled.app.viewmodel.ProjectViewModel
import com.fabled.app.viewmodel.WorldBuildingViewModel
import org.koin.dsl.module

val appModule = module {
    single { createFabledDatabase() }
    single { ProjectRepositoryImpl(get()) }
    single { SceneRepositoryImpl(get()) }
    single { CharacterRepositoryImpl(get()) }
    single { WorldBuildingRepositoryImpl(get()) }

    factory { CreateProjectUseCase(get<ProjectRepositoryImpl>()) }
    factory { GetProjectsUseCase(get<ProjectRepositoryImpl>()) }
    factory { DeleteProjectUseCase(get<ProjectRepositoryImpl>()) }
    factory { CreateSceneUseCase(get<SceneRepositoryImpl>()) }
    factory { UpdateSceneUseCase(get<SceneRepositoryImpl>()) }
    factory { GetScenesForChapterUseCase(get<SceneRepositoryImpl>()) }
    factory { GenerateSuggestionUseCase() }
    factory { ContinuityCheckUseCase() }

    factory { ProjectViewModel(get(), get(), get()) }
    factory { DraftingViewModel(get(), get(), get()) }
    factory { WorldBuildingViewModel(get()) }
    factory { AssistantViewModel(get(), get()) }
}
