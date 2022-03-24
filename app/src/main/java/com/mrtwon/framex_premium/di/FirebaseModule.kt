package com.mrtwon.framex_premium.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mrtwon.framex_premium.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @AppScope
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore{
        return Firebase.firestore
    }
}