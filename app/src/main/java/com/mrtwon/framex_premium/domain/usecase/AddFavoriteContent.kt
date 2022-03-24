package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.Favorite
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.ContentRepository
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class AddFavoriteContent @Inject constructor(
    val contentRepository: ContentRepository,
    val databaseRepository: DatabaseRepository
    ): UseCase<Unit, AddFavoriteContent.Param>() {

    data class Param(
        val idContent: Int, val contentType: ContentEnum
    )
    data class SecondaryParam(val favorite: Favorite)

    override suspend fun run(params: Param): Either<Failure, Unit> {
        val resultContent = contentRepository.getContentById(
            GetContentById.Params(params.idContent, params.contentType)
        )
        if(resultContent.isLeft) return resultContent as Either.Left<Failure>

        val resultContentRight = (resultContent as Either.Right<Content?>).b ?: return Either.Left(Failure.ServerError)
        return databaseRepository.addFavorite(
            AddFavoriteContent.SecondaryParam(
                favorite = Favorite(
                    idContent = resultContentRight.id ?: return Either.Left(Failure.ServerError),
                    ruTitle = resultContentRight.ruTitle,
                    contentType = resultContentRight.contentType,
                    posterPreview = resultContentRight.posterPreview
                )
            )
        )
    }
}