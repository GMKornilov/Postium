package com.gmkornilov.postcreatepage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.postcreatepage.view.PostCreate
import com.gmkornilov.postcreatepage.view.PostCreateViewModel
import javax.inject.Inject

private const val POST_CREATE_PAGE_KEY = "create post"

class PostCreatePageScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<PostCreatePageScreenFactory.Deps> {
    private val screen = BaseScreen(
        POST_CREATE_PAGE_KEY,
        onCreate = { _, _ ->
            val component = DaggerPostCreatePageScreenFactory_Component.builder()
                .deps(dependency)
                .build()
            component.postCreateViewModel
        }
    ) {
        val viewModel = it.get<PostCreateViewModel>()
        PostCreate(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(): Screen<*> {
        return screen
    }

    interface Deps: Dependency {

    }

    @dagger.Component(
        dependencies = [Deps::class]
    )
    internal interface Component {
        val postCreateViewModel: PostCreateViewModel
    }
}