package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class ExistSubscription @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<Boolean, ExistSubscription.Param>() {
    data class Param(val contentId: Int)

    override suspend fun run(params: Param): Either<Failure, Boolean> {
        return databaseRepository.existSubscription(param = params)
    }
}