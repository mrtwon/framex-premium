package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.ContentRepository
import javax.inject.Inject

class GetContentById @Inject constructor(val repository: ContentRepository): UseCase<Content?, GetContentById.Params>() {

    class Params(val id: Int, val content: ContentEnum)

    override suspend fun run(params: Params): Either<Failure, Content?> {
        return repository.getContentById(params)
    }
}