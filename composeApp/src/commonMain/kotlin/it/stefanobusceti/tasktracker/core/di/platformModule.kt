package it.stefanobusceti.tasktracker.core.di

import it.stefanobusceti.tasktracker.core.data.database.DatabaseConstructor
import it.stefanobusceti.tasktracker.core.data.database.TaskDao
import it.stefanobusceti.tasktracker.core.data.database.TaskDatabase
import it.stefanobusceti.tasktracker.core.data.repository.TaskRepositoryImpl
import it.stefanobusceti.tasktracker.core.domain.TaskRepository
import it.stefanobusceti.tasktracker.core.domain.usecase.AddTaskUseCase
import it.stefanobusceti.tasktracker.core.domain.usecase.ToggleRunningUseCase
import it.stefanobusceti.tasktracker.feature.main.presentation.MainScreenViewModel
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
    //Database
    single<TaskDatabase> { DatabaseConstructor.initialize() }
    single<TaskDao> { get<TaskDatabase>().taskDao() }
}