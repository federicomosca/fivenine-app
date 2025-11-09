package me.dogs.fivenine.domain.usecase

import me.dogs.fivenine.domain.model.List
import me.dogs.fivenine.domain.repository.ListRepository

/**
 * Example Use Case
 */
class GetListsUseCase(
    private val repository: ListRepository
) {
    suspend operator fun invoke(): kotlin.collections.List<List> {
        return repository.getUsers()
    }
}
