package com.mrtwon.framex_premium.domain.repository

import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.usecase.GetContentByDescriptionPaging
import com.mrtwon.framex_premium.domain.usecase.GetContentByTitlePaging
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByGenresPaging
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByYearPaging

interface PagingRepository {
    fun getTopContentByYear(request: GetTopContentByYearPaging.Params): Either<Failure, List<ContentItemPage>>
    fun getTopContentByGenres(request: GetTopContentByGenresPaging.Params): Either<Failure, List<ContentItemPage>>
    fun getContentByDescription(request: GetContentByDescriptionPaging.Params): Either<Failure, List<ContentItemPage>>
    fun getContentByTitle(request: GetContentByTitlePaging.Params): Either<Failure, List<ContentItemPage>>
}