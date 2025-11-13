package com.stayclean.shared.di

import com.stayclean.infrastructure.repository.UserRepositoryImpl
import com.stayclean.application.usecase.GetUserUseCase

/**
 * Wiring simple para usar en la capa de presentación dentro de `app`.
 */
object ServiceLocator {
    // Tipos explícitos para evitar que el compilador no pueda inferir T en 'by lazy'
    val userRepository: UserRepositoryImpl by lazy { UserRepositoryImpl() }
    val getUserUseCase: GetUserUseCase by lazy { GetUserUseCase(userRepository) }
}
