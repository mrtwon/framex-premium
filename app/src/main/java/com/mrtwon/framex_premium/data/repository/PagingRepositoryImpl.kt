package com.mrtwon.framex_premium.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.mrtwon.framex_premium.data.functional.requestList
import com.mrtwon.framex_premium.data.functional.transformToContentItemPage

import com.mrtwon.framex_premium.data.networkDataSource.firebaseSource.FirebaseDataSource
import com.mrtwon.framex_premium.data.networkDataSource.firebaseSource.WrapperResponse
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.PagingRepository
import com.mrtwon.framex_premium.domain.usecase.GetContentByDescriptionPaging
import com.mrtwon.framex_premium.domain.usecase.GetContentByTitlePaging
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByGenresPaging
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByYearPaging
import java.lang.Exception
import javax.inject.Inject

class PagingRepositoryImpl @Inject constructor(val firebaseSource: FirebaseDataSource): PagingRepository {
    override fun getTopContentByYear(request: GetTopContentByYearPaging.Params): Either<Failure, List<ContentItemPage>> {

        return requestList(
            firebaseSource.getTopByYear(param = request),
            ::transformToContentItemPage
        )
    }

    override fun getTopContentByGenres(request: GetTopContentByGenresPaging.Params): Either<Failure, List<ContentItemPage>> {
        return requestList(
                firebaseSource.getTopByGenres(request),
                ::transformToContentItemPage
        )
    }

    override fun getContentByDescription(request: GetContentByDescriptionPaging.Params): Either<Failure, List<ContentItemPage>> {
        return requestList(
            firebaseSource.getContentByDescription(request),
            ::transformToContentItemPage
        )
    }

    override fun getContentByTitle(request: GetContentByTitlePaging.Params): Either<Failure, List<ContentItemPage>> {
        return requestList(
            firebaseSource.getContentByTitle(request),
            ::transformToContentItemPage
        )
    }
}