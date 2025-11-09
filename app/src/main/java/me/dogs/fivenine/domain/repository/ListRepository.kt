package me.dogs.fivenine.domain.repository

import me.dogs.fivenine.domain.model.List

/**
 * Example Repository Interface
 */
interface ListRepository {
    suspend fun getUsers(): kotlin.collections.List<List>
    suspend fun getUserById(id: Int): List?
}
