package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.PagingRepository
import javax.inject.Inject

class GetContentByDescriptionPaging @Inject constructor(val repository: PagingRepository): UseCase<List<ContentItemPage>, GetContentByDescriptionPaging.Params>() {
    class PrimaryParam(val descriptionList: List<String>, val sortBy: RatingEnum = RatingEnum.Kinopoisk)
    class Params(val primaryParam: PrimaryParam, val itemKey: Any?, val limit: Long, val position: StartPosition)

    override suspend fun run(params: Params): Either<Failure, List<ContentItemPage>> {
        return repository.getContentByDescription(params)
    }
}