package me.dogs.fivenine.data.repository

import me.dogs.fivenine.domain.model.List
import me.dogs.fivenine.domain.repository.ListRepository

/**
 * Example Repository Implementation
 */
class ListRepositoryImpl : ListRepository {
    // Implement repository methods here
    override suspend fun getUsers(): kotlin.collections.List<List> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: Int): List? {
        TODO("Not yet implemented")
    }
}
