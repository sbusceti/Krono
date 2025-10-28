package it.stefanobusceti.krono

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform