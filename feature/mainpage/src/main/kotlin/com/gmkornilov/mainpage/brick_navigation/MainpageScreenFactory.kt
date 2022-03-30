package com.gmkornilov.mainpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.mainpage.mainpage.MainPageListener
import com.gmkornilov.mainpage.mainpage.MainPageViewModel
import com.gmkornilov.mainpage.mainpage.Mainpage
import com.gmkornilov.post.repository.PostRepository
import dagger.BindsInstance
import javax.inject.Inject
import javax.inject.Scope

class MainpageScreenFactory @Inject constructor(
    override val dependency: Deps,
) : NavigationScreenProvider<MainpageScreenFactory.Deps> {
    private lateinit var listener: MainPageListener

    private val mainpageScreen = BaseScreen(
        key = "Home",
        onCreate = { _, _ ->
            val component = DaggerMainpageScreenFactory_Component.builder()
                .deps(dependency)
                .listener(listener)
                .build()
            component.mainPageViewModel
        },
        content = {
            val viewModel = it.get<MainPageViewModel>()
            Mainpage(viewModel, Modifier.fillMaxSize())
        }
    )

    interface Deps : Dependency {
        val postRepository: PostRepository

        val authInteractor: AuthInteractor
    }

    @Scope
    annotation class MainpageScope

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class]
    )
    @MainpageScope
    internal interface Component {
        val mainPageViewModel: MainPageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun listener(listener: MainPageListener): Builder

            fun deps(deps: Deps): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    internal interface Module {

    }

    fun build(listener: MainPageListener): Screen<*> {
        this.listener = listener
        return mainpageScreen
    }
}