package com.gmkornilov.postpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.postpage.view.Postpage
import com.gmkornilov.postpage.view.PostpageListener
import com.gmkornilov.postpage.view.PostpageViewModel
import dagger.BindsInstance
import javax.inject.Inject
import javax.inject.Scope

private const val POST_PAGE_SCREEN_KEY = "post_page"

class PostPageScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<PostPageScreenFactory.Deps> {
    private lateinit var listener: PostpageListener

    private val screen = BaseScreen(
        key = POST_PAGE_SCREEN_KEY,
        onCreate = { _, arg ->
            val argument = arg.get<PostPageArgument>()

            val component = DaggerPostPageScreenFactory_Component.builder()
                .postPageArgument(argument)
                .listener(listener)
                .deps(dependency)
                .build()

            component.postpageViewModel
        }
    ) {
        val viewModel = it.get<PostpageViewModel>()
        Postpage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(listener: PostpageListener): Screen<*> {
        this.listener = listener
        return screen
    }

    interface Deps: Dependency {
        val postContentsRepository: PostContentsRepository
        val postRepository: PostRepository

        val authInteractor: AuthInteractor
    }

    @Scope
    annotation class PostPageScope

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class]
    )
    @PostPageScope
    internal interface Component {
        val postpageViewModel: PostpageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun postPageArgument(argument: PostPageArgument): Builder

            fun deps(dependency: Deps): Builder

            @BindsInstance
            fun listener(postpageListener: PostpageListener): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    internal interface Module {

    }
}