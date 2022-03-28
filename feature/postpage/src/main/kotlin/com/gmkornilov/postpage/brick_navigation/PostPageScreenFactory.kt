package com.gmkornilov.postpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.BaseScreen
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.postpage.postpage.Postpage
import com.gmkornilov.postpage.postpage.PostpageViewModel
import dagger.BindsInstance
import javax.inject.Inject

private const val POST_PAGE_SCREEN_KEY = "post_page"

class PostPageScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<PostPageScreenFactory.Deps> {
    private val screen = BaseScreen(
        key = POST_PAGE_SCREEN_KEY,
        onCreate = { _, arg ->
            val postPageArgument = arg.get<PostPageArgument>()

            val component = DaggerPostPageScreenFactory_Component.builder()
                .postPageArgument(postPageArgument)
                .deps(dependency)
                .build()

            component.postpageViewModel
        }
    ) {
        val viewModel = it.get<PostpageViewModel>()
        Postpage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(): BaseScreen<*> {
        return screen
    }

    interface Deps: Dependency {
        val postContentsRepository: PostContentsRepository
        val postBookmarkRepository: PostBookmarkRepository
        val postLikeRepository: PostLikeRepository
    }

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class]
    )
    internal interface Component {
        val postpageViewModel: PostpageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun postPageArgument(argument: PostPageArgument): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    internal interface Module {

    }
}