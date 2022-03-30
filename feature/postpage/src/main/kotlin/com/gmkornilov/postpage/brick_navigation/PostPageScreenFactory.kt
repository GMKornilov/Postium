package com.gmkornilov.postpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.postpage.view.Postpage
import com.gmkornilov.postpage.view.PostpageListener
import com.gmkornilov.postpage.view.PostpageViewModel
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val POST_PAGE_SCREEN_KEY = "post_page"

class PostPageScreenFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<PostPageScreenFactory.Deps> {
    private inner class Factory(
        val listener: PostpageListener,
        val postPageArgument: PostPageArgument,
    ) : ScreenFactory() {
        override fun buildKey(): String {
            return "${POST_PAGE_SCREEN_KEY}_${postPageArgument.id}"
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerPostPageScreenFactory_Component.builder()
                .postPageArgument(postPageArgument)
                .listener(listener)
                .deps(dependency)
                .build()

            return component.postpageViewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<PostpageViewModel>()
            Postpage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }

    fun build(
        listener: PostpageListener,
        postPageArgument: PostPageArgument,
        prevPath: String
    ): Screen<*> {
        return Factory(listener, postPageArgument).build(prevPath)
    }

    interface Deps : Dependency {
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