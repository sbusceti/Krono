package it.stefanobusceti.krono.domain

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class Task @OptIn(ExperimentalTime::class) constructor(
    val id: Int,
    val name: String,
    var startTime: Long = Clock.System.now().toEpochMilliseconds(),
    var running: Boolean = true,
    var totalTime: Long = 0
)