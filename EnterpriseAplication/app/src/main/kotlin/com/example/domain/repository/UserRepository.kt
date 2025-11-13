package com.stayclean.domain.repository

import com.stayclean.domain.model.User

interface UserRepository {
    suspend fun findById(id: String): User?
    suspend fun save(user: User)
}
