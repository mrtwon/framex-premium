package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class RemoveSubscription @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<Unit, RemoveSubscription.Param>() {
    data class Param(val id: Int)

    override suspend fun run(params: Param): Either<Failure, Unit> {
        return databaseRepository.removeSubscription(params)
    }
}