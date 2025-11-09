package me.dogs.fivenine.domain.usecase

import me.dogs.fivenine.domain.model.User
import me.dogs.fivenine.domain.repository.UserRepository

/**
 * Example Use Case
 */
class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return repository.getUsers()
    }
}
