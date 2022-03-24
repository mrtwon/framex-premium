package com.mrtwon.framex_premium.data.repository

import com.mrtwon.framex_premium.data.functional.requestOneElement
import com.mrtwon.framex_premium.data.functional.transformToContent
import com.mrtwon.framex_premium.data.networkDataSource.firebaseSource.FirebaseDataSource
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.ContentRepository
import com.mrtwon.framex_premium.domain.usecase.GetContentById
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(val firebaseSource: FirebaseDataSource): ContentRepository {
    override fun getContentById(request: GetContentById.Params): Either<Failure, Content?> {
        return requestOneElement(
            firebaseSource.getContentById(request.id, request.content),
            ::transformToContent
        )
    }
}