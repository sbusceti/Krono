package it.stefanobusceti.krono.feature.main.presentation

sealed interface UiEvent {
     data class SaveInProgress(val saveInProgress: Boolean) : UiEvent
 }