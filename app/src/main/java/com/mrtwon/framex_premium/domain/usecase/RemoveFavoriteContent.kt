package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class RemoveFavoriteContent @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<Unit, RemoveFavoriteContent.Param>() {
    data class Param(val id: Int, val contentEnum: ContentEnum)

    override suspend fun run(params: Param): Either<Failure, Unit> {
        return databaseRepository.removeFavorite(params)
    }
}