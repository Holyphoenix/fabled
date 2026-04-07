package com.fabled.app.di

import com.fabled.data.database.createFabledDatabase
import com.fabled.data.repository.ChapterRepositoryImpl
import com.fabled.data.repository.CharacterRepositoryImpl
import com.fabled.data.repository.ProjectRepositoryImpl
import com.fabled.data.repository.SceneRepositoryImpl
import com.fabled.data.repository.TimelineRepositoryImpl
import com.fabled.data.repository.WorldBuildingRepositoryImpl
import com.fabled.shared.domain.usecase.assistant.ContinuityCheckUseCase
import com.fabled.shared.domain.usecase.assistant.GenerateSuggestionUseCase
import com.fabled.shared.domain.usecase.chapter.CreateChapterUseCase
import com.fabled.shared.domain.usecase.chapter.DeleteChapterUseCase
import com.fabled.shared.domain.usecase.chapter.GetChaptersUseCase
import com.fabled.shared.domain.usecase.character.CreateCharacterUseCase
import com.fabled.shared.domain.usecase.character.UpdateCharacterUseCase
import com.fabled.shared.domain.usecase.location.CreateLocationUseCase
import com.fabled.shared.domain.usecase.location.UpdateLocationUseCase
import com.fabled.shared.domain.usecase.project.CreateProjectUseCase
import com.fabled.shared.domain.usecase.project.DeleteProjectUseCase
import com.fabled.shared.domain.usecase.project.GetProjectsUseCase
import com.fabled.shared.domain.usecase.project.UpdateProjectUseCase
import com.fabled.shared.domain.usecase.scene.CreateSceneUseCase
import com.fabled.shared.domain.usecase.scene.GetScenesForChapterUseCase
import com.fabled.shared.domain.usecase.scene.UpdateSceneUseCase
import com.fabled.shared.domain.usecase.timeline.CreateTimelineEventUseCase
import com.fabled.shared.domain.usecase.timeline.GetTimelineEventsUseCase
import com.fabled.app.viewmodel.AssistantViewModel
import com.fabled.app.viewmodel.DraftingViewModel
import com.fabled.app.viewmodel.ProjectViewModel
import com.fabled.app.viewmodel.TimelineViewModel
import com.fabled.app.viewmodel.WorldBuildingViewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single { createFabledDatabase() }

    // Repositories
    single { ProjectRepositoryImpl(get()) }
    single { ChapterRepositoryImpl(get()) }
    single { SceneRepositoryImpl(get()) }
    single { CharacterRepositoryImpl(get()) }
    single { WorldBuildingRepositoryImpl(get()) }
    single { TimelineRepositoryImpl(get()) }

    // Project use cases
    factory { CreateProjectUseCase(get<ProjectRepositoryImpl>()) }
    factory { GetProjectsUseCase(get<ProjectRepositoryImpl>()) }
    factory { DeleteProjectUseCase(get<ProjectRepositoryImpl>()) }
    factory { UpdateProjectUseCase(get<ProjectRepositoryImpl>()) }

    // Chapter use cases
    factory { GetChaptersUseCase(get<ChapterRepositoryImpl>()) }
    factory { CreateChapterUseCase(get<ChapterRepositoryImpl>()) }
    factory { DeleteChapterUseCase(get<ChapterRepositoryImpl>()) }

    // Scene use cases
    factory { CreateSceneUseCase(get<SceneRepositoryImpl>()) }
    factory { UpdateSceneUseCase(get<SceneRepositoryImpl>()) }
    factory { GetScenesForChapterUseCase(get<SceneRepositoryImpl>()) }

    // Character use cases
    factory { CreateCharacterUseCase(get<CharacterRepositoryImpl>()) }
    factory { UpdateCharacterUseCase(get<CharacterRepositoryImpl>()) }

    // Location use cases
    factory { CreateLocationUseCase(get<WorldBuildingRepositoryImpl>()) }
    factory { UpdateLocationUseCase(get<WorldBuildingRepositoryImpl>()) }

    // Timeline use cases
    factory { CreateTimelineEventUseCase(get<TimelineRepositoryImpl>()) }
    factory { GetTimelineEventsUseCase(get<TimelineRepositoryImpl>()) }

    // Assistant use cases
    factory { GenerateSuggestionUseCase() }
    factory { ContinuityCheckUseCase() }

    // ViewModels
    factory { ProjectViewModel(get(), get(), get(), get()) }
    factory {
        DraftingViewModel(
            get(), get(), get(), get(), get(), get(), get(),
            get<ProjectRepositoryImpl>()
        )
    }
    factory {
        WorldBuildingViewModel(
            get<CharacterRepositoryImpl>(),
            get<WorldBuildingRepositoryImpl>(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { AssistantViewModel(get(), get()) }
    factory {
        TimelineViewModel(
            get(),
            get(),
            get<TimelineRepositoryImpl>()
        )
    }
}

