package com.gmkornilov.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PostsModule {
    @Singleton
    @Provides
    fun firestore(): FirebaseFirestore {
        return Firebase.firestore
    }
}