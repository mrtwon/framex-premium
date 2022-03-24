package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.PagingRepository
import javax.inject.Inject

class GetTopContentByGenresPaging @Inject constructor(val repository: PagingRepository): UseCase<List<ContentItemPage>, GetTopContentByGenresPaging.Params>() {

    class PrimaryParams(val content: ContentEnum, val sortBy: RatingEnum, val genres: GenresEnum)
    class Params(val primaryParam: PrimaryParams, val position: StartPosition, val itemKey: Any?, val limit: Long)
    override suspend fun run(params: Params): Either<Failure, List<ContentItemPage>> {
        return repository.getTopContentByGenres(params)
    }
}