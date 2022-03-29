package com.gmkornilov.post.di

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.source.FirebasePostSource
import dagger.Provides
import javax.inject.Singleton

@dagger.Module
class PostRepositoryModule {
    @Singleton
    @Provides
    fun providePostRepository(
        firebasePostSource: FirebasePostSource,
        likeRepository: PostLikeRepository,
        authInteractor: AuthInteractor,
        bookmarkRepository: PostBookmarkRepository,
    ): PostRepository {
        return PostRepository(
            firebasePostSource,
            likeRepository,
            authInteractor,
            bookmarkRepository
        )
    }
}