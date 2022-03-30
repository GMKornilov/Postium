package com.gmkornilov.mainpage.brick_navigation

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
import com.gmkornilov.mainpage.mainpage.MainPageListener
import com.gmkornilov.mainpage.mainpage.MainPageViewModel
import com.gmkornilov.mainpage.mainpage.Mainpage
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val HOME_KEY = "home"

class MainpageScreenFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<MainpageScreenFactory.Deps> {
    private inner class Factory(
        private val listener: MainPageListener,
    ): ScreenFactory() {
        override fun buildKey(): String {
            return HOME_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerMainpageScreenFactory_Component.builder()
                .deps(dependency)
                .listener(listener)
                .build()
            return component.mainPageViewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<MainPageViewModel>()
            Mainpage(viewModel, Modifier.fillMaxSize())
        }

    }

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

    fun build(listener: MainPageListener, prevPath: String): Screen<*> {
        return Factory(listener).build(prevPath)
    }
}