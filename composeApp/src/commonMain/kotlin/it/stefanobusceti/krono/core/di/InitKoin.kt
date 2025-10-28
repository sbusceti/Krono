package it.stefanobusceti.krono.core.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * @author: Stefano Busceti
 * @version: 1.0 14/08/2025
 */

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            //platformModule
        )
    }
}