package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class UpdateRecentContent @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<Unit, UpdateRecentContent.Param>() {
    data class Param(val recent: Recent)

    override suspend fun run(params: Param): Either<Failure, Unit> {
        return databaseRepository.updateRecent(params)
    }

}