package com.mrtwon.framex_premium.paging

import androidx.paging.PagedList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

interface PagedFacade {
    fun getPagedList(firestore: FirebaseFirestore): PagedList<DocumentSnapshot>
}