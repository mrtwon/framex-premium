package com.mrtwon.framex_premium.modules

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {
    @Singleton
    @Provides
    fun getFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}