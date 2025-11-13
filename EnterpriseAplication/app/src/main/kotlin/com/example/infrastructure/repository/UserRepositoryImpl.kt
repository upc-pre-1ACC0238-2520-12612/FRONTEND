package com.stayclean.infrastructure.repository

import com.stayclean.domain.model.User
import com.stayclean.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val storage = mutableMapOf<String, User>()

    override suspend fun findById(id: String): User? = storage[id]

    override suspend fun save(user: User) {
        storage[user.id] = user
    }
}
