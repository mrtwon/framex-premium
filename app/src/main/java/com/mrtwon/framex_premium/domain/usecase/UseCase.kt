package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<Type, Params> {
    abstract suspend fun run(params: Params): Either<Failure, Type>


    operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ){
        scope.launch(Dispatchers.Main){
            val deferred = scope.async(Dispatchers.IO) { run(params) }
            onResult(deferred.await())
        }
    }
}