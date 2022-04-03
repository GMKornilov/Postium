package com.gmkornilov.di

import android.content.Context
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.data.AuthModule
import com.gmkornilov.authorizarion.email.EmailModule
import com.gmkornilov.authorizarion.facebook.FacebookModule
import com.gmkornilov.authorizarion.firebase.FirebaseModule
import com.gmkornilov.authorizarion.google.GoogleModule
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.comment_likes.repository.CommentLikesRepository
import com.gmkornilov.context.ApplicationContext
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_comments.repository.PostCommentRepository
import com.gmkornilov.comments.repostiory.CommentRepository
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.secrets.SecretsModule
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.strings.StringsProvider
import com.gmkornilov.user.repository.UserRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        DepsModule::class,
        AuthModule::class,
        EmailModule::class,
        FacebookModule::class,
        FirebaseModule::class,
        GoogleModule::class,
        SecretsModule::class,
        NavigationModule::class,
        PostsModule::class,
    ]
)
interface SingletonModule {
    companion object {
        @Singleton
        @Provides
        fun activityHelper() = ActivityHelper()

        @Singleton
        @Provides
        fun stringsProvider(@ApplicationContext context: Context) = StringsProvider(context)

        @Singleton
        @Provides
        fun firebaseStoreage() = FirebaseStorage.getInstance()

        @Singleton
        @Provides
        fun providePostRepository(
            firebasePostSource: FirebasePostSource,
            likeRepository: PostLikeRepository,
            authInteractor: AuthInteractor,
            bookmarkRepository: PostBookmarkRepository,
            userRepository: UserRepository,
            categoryRepository: CategoriesRepository,
        ): PostRepository {
            return PostRepository(
                firebasePostSource,
                likeRepository,
                authInteractor,
                bookmarkRepository,
                userRepository
            )
        }

        @Singleton
        @Provides
        fun provideCommentsRepository(
            commentLikesRepository: CommentLikesRepository,
            postCommentRepository: PostCommentRepository,
            userRepository: UserRepository,
            authInteractor: AuthInteractor
        ): CommentRepository {
            return CommentRepository(
                commentLikesRepository,
                postCommentRepository,
                userRepository,
                authInteractor
            )
        }
    }
}