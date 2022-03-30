package com.gmkornilov.postcreatepage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.postcreatepage.view.PostCreate
import com.gmkornilov.postcreatepage.view.PostCreateListener
import com.gmkornilov.postcreatepage.view.PostCreateViewModel
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.user.repository.UserRepository
import dagger.BindsInstance
import javax.inject.Inject
import javax.inject.Scope

private const val POST_CREATE_PAGE_KEY = "create post"

class PostCreatePageScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<PostCreatePageScreenFactory.Deps> {
    private lateinit var listener: PostCreateListener

    private val screen = BaseScreen(
        POST_CREATE_PAGE_KEY,
        onCreate = { _, _ ->
            val component = DaggerPostCreatePageScreenFactory_Component.builder()
                .listener(listener)
                .deps(dependency)
                .build()
            component.postCreateViewModel
        }
    ) {
        val viewModel = it.get<PostCreateViewModel>()
        PostCreate(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(listener: PostCreateListener): Screen<*> {
        this.listener = listener
        return screen
    }

    interface Deps: Dependency {
        val firebasePostSource: FirebasePostSource
        val postContentsRepository: PostContentsRepository
        val userRepository: UserRepository
        val authInteractor: AuthInteractor
    }

    @Scope
    annotation class PostCreateScope

    @dagger.Component(
        dependencies = [Deps::class]
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
}