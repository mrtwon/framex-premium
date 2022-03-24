package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.PagingRepository
import javax.inject.Inject

class GetTopContentByYearPaging @Inject constructor(val repository: PagingRepository)
    : UseCase<List<ContentItemPage>, GetTopContentByYearPaging.Params>() {

    data class PrimaryParams(
        val sortBy: RatingEnum = RatingEnum.None,
        val genresEnum: GenresEnum = GenresEnum.None,
        val content: ContentEnum,
        val year: Int
    )
    data class Params(val primaryParam: PrimaryParams, val itemKey: Any?, val position: StartPosition, val limit: Long)

    override suspend fun run(params: Params): Either<Failure, List<ContentItemPage>> {
        return repository.getTopContentByYear(params)
    }
}