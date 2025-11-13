package com.stayclean.application.usecase

import com.stayclean.domain.repository.UserRepository
import com.stayclean.domain.model.User

class GetUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: String): User? {
        if (id.isBlank()) return null
        return repository.findById(id)
    }
}
