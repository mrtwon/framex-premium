package com.mrtwon.framex_premium.data.extenstion

import com.google.firebase.firestore.DocumentSnapshot
import com.mrtwon.framex_premium.data.entity.ContentEntity
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.ContentItemPage

fun DocumentSnapshot.toContentItemPage(fromCache: Boolean): ContentItemPage?{
    val content = toObject(ContentEntity::class.java)?.toContent()
    if(content != null){
        return ContentItemPage(content, this, fromCache)
    }
    return null
}

fun DocumentSnapshot.toContent(): Content? {
    return toObject(ContentEntity::class.java)?.toContent()
}