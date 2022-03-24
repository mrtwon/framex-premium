package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class UpdateSubscriptionContent @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<Unit, UpdateSubscriptionContent.Param>() {
    data class Param(val subscription: Subscription)

    override suspend fun run(params: Param): Either<Failure, Unit> {
        return databaseRepository.updateSubscription(params)
    }
}