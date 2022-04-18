package com.gmkornilov.postcreatepage.brick_navigation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.postcreatepage.data.DB_NAME
import com.gmkornilov.postcreatepage.view.PostCreate
import com.gmkornilov.postcreatepage.view.PostCreateListener
import com.gmkornilov.postcreatepage.view.PostCreateViewModel
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.user.repository.UserRepository
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import dagger.Provides
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val POST_CREATE_PAGE_KEY = "create_post"

class PostCreatePageScreenFactory @Inject constructor(
    override val dependency: Deps,
): DependencyProvider<PostCreatePageScreenFactory.Deps> {
    private inner class Factory(
        val listener: PostCreateListener,
    ): ScreenFactory() {
        override fun buildKey(): String {
            return POST_CREATE_PAGE_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerPostCreatePageScreenFactory_Component.builder()
                .listener(listener)
                .deps(dependency)
                .build()
            return component.postCreateViewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<PostCreateViewModel>()
            PostCreate(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }

    }

    fun build(listener: PostCreateListener, prevPath: String): Screen<*> {
        return Factory(listener).build(prevPath)
    }

    interface Deps: Dependency {
        val firebasePostSource: FirebasePostSource
        val postContentsRepository: PostContentsRepository
        val userRepository: UserRepository
        val authInteractor: AuthInteractor
        val categoriesRepository: CategoriesRepository

        val context: Context
    }

    @Scope
    annotation class PostCreateScope

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class],
    )
    @PostCreateScope
    internal interface Component {
        val postCreateViewModel: PostCreateViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun listener(listener: PostCreateListener): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    interface Module {
        companion object {
            private val Context.draftsStore by preferencesDataStore(DB_NAME)

            @PostCreateScope
            @Provides
            fun dataStore(context: Context): DataStore<Preferences> {
                return context.draftsStore
            }
        }
    }
}