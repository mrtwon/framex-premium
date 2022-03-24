package com.mrtwon.framex_premium.data.functional

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.mrtwon.framex_premium.data.extenstion.toContent
import com.mrtwon.framex_premium.data.extenstion.toContentItemPage
import com.mrtwon.framex_premium.data.networkDataSource.firebaseSource.WrapperResponse
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import java.lang.Exception

fun <T> requestOneElement(
    call: () -> Task<QuerySnapshot>,
    transform: (List<DocumentSnapshot>) -> T?
): Either<Failure, T?> {
    return try{
        val callResult = call()
        if(callResult.isSuccessful){
            Either.Right(transform(callResult.result.documents))
        }else{
            Either.Left(Failure.ServerError)
        }
    }catch (e: Exception){
        e.printStackTrace()
        Either.Left(Failure.ServerError)
    }
}

/*fun <T> requestList (
    call: Task<QuerySnapshot>,
    transform: (List<DocumentSnapshot>) -> List<T>
): Either<Failure, List<T>> {
    return try{
        if(call.isSuccessful){
            Either.Right(transform(call.result.documents))
        }else {
            Log.i("self-debug","not Successful result")
            Either.Left(Failure.ServerError)
        }
    }catch (e: Exception){
        e.printStackTrace()
        Either.Left(Failure.ServerError)
    }
}*/
/*fun <T> requestList (
    wrapper: WrapperResponse,
    transform: (List<DocumentSnapshot>, fromCache: Boolean) -> List<T>
): Either<Failure, List<T>> {
    return try{
        if(wrapper.task.exception != null)
            throw wrapper.task.exception!!
        if(wrapper.task.isSuccessful){
            *//*Log.i("self-retry","Successful exception ${wrapper.task.exception == null}")
            wrapper.task.exception?.printStackTrace()*//*
            Either.Right(transform(wrapper.task.result.documents, wrapper.fromCache))
        }else {
            Log.i("self-debug","not Successful result")
            Either.Left(Failure.ServerError)
        }
    }catch (e: Exception){
        Log.i("self-retry","Exception")
        e.printStackTrace()
        Either.Left(Failure.ServerError)
    }
}*/
fun <T> requestList (
    wrapperCallback: () -> WrapperResponse,
    transform: (List<DocumentSnapshot>, fromCache: Boolean) -> List<T>
): Either<Failure, List<T>> {
    return try{
        val wrapper = wrapperCallback()
        Log.i("self-debug","testReq size ${wrapper.task.result.documents.size} exception ${wrapper.task.exception == null}")
        if(wrapper.task.exception != null)
            throw wrapper.task.exception!!
        if(wrapper.task.isSuccessful){
            Either.Right(transform(wrapper.task.result.documents, wrapper.fromCache))
        }else {
            Log.i("self-debug","not Successful result")
            Either.Left(Failure.ServerError)
        }
    }catch (e: Exception){
        Log.i("self-retry","Exception")
        e.printStackTrace()
        Either.Left(Failure.ServerError)
    }
}

 fun transformToContentItemPage(input: List<DocumentSnapshot>, fromCache: Boolean): List<ContentItemPage>{
    return arrayListOf<ContentItemPage>().apply {
        input.forEach{ snapshot ->
            snapshot.toContentItemPage(fromCache)?.let {
                add(it)
            }
        }
    }
}

 fun transformToContent(input: List<DocumentSnapshot>): Content?{
    return if(input.isNotEmpty())
        input[0].toContent()
    else null
}