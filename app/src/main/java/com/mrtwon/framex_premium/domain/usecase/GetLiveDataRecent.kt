package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetLiveDataRecent  @Inject constructor(val databaseRepository: DatabaseRepository)
    : UseCase<SubscriptionInterface<List<Recent>>, Unit>() {
    override suspend fun run(params: Unit): Either<Failure, SubscriptionInterface<List<Recent>>> {
        return databaseRepository.getRecentLiveData()
    }
}