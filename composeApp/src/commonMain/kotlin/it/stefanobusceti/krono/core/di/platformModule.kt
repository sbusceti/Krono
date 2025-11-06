package it.stefanobusceti.krono.core.di

import it.stefanobusceti.krono.core.data.database.DatabaseConstructor
import it.stefanobusceti.krono.core.data.database.TaskDao
import it.stefanobusceti.krono.core.data.database.TaskDatabase
import it.stefanobusceti.krono.core.data.repository.TaskRepositoryImpl
import it.stefanobusceti.krono.core.domain.TaskRepository
import it.stefanobusceti.krono.core.domain.usecase.AddTaskUseCase
import it.stefanobusceti.krono.core.domain.usecase.DeleteTaskUseCase
import it.stefanobusceti.krono.core.domain.usecase.ToggleRunningUseCase
import it.stefanobusceti.krono.feature.main.presentation.MainScreenViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    //Repository
    singleOf(::TaskRepositoryImpl).bind<TaskRepository>()
    //ViewModel
    viewModelOf(::MainScreenViewModel)
    //UseCase
    factory<ToggleRunningUseCase> { ToggleRunningUseCase(get()) }
    factory<AddTaskUseCase> { AddTaskUseCase(get()) }
    factory<DeleteTaskUseCase> { DeleteTaskUseCase(get()) }
    //Database
    single<TaskDatabase> { DatabaseConstructor.initialize() }
    single<TaskDao> { get<TaskDatabase>().taskDao() }
}