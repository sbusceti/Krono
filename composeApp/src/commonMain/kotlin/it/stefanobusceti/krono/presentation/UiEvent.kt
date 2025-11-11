package it.stefanobusceti.krono.presentation

sealed interface UiEvent {
     data class SaveInProgress(val saveInProgress: Boolean) : UiEvent
 }