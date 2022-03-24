package com.mrtwon.testfirebase.paging

import com.google.firebase.firestore.DocumentSnapshot

interface  FirebaseStorage <T, R> {
    fun after(document: T, limit: Long, callback: (List<R>) -> Unit)
    fun before(document: T, limit: Long, callback: (List<R>) -> Unit)
    fun first(limit: Long, callback: (List<R>) -> Unit)
}