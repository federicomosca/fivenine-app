package me.dogs.fivenine.domain.repository

import me.dogs.fivenine.domain.model.User

/**
 * Example Repository Interface
 */
interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: Int): User?
}
