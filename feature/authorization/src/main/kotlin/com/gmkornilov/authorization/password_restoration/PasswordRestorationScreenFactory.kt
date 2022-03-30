package com.gmkornilov.authorization.password_restoration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.password_restoration.domain.PasswordRestorationFlowEvents
import com.gmkornilov.authorization.password_restoration.view.PasswordRestoration
import com.gmkornilov.authorization.password_restoration.view.PasswordRestorationViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.strings.StringsProvider
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val PASSWORD_RESTORATION_KEY = "password_restoration"

internal class PasswordRestorationScreenFactory @Inject constructor(
    override val dependency: Deps,
): DependencyProvider<PasswordRestorationScreenFactory.Deps> {
    private inner class Factory: ScreenFactory() {
        override fun buildKey(): String {
            return PASSWORD_RESTORATION_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerPasswordRestorationScreenFactory_Component.builder()
                .deps(dependency)
                .build()
            return component.passwordRestorationViewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<PasswordRestorationViewModel>()
            PasswordRestoration(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }

    fun build(prevPath: String): Screen<*> {
        return Factory().build(prevPath)
    }

    interface Deps: Dependency {
        val authInteractor: AuthInteractor

        val stringProvider: StringsProvider

        val passwordRestorationFlowEvents: PasswordRestorationFlowEvents
    }

    @Scope
    annotation class PasswordRestorationScope

    @dagger.Component(
        dependencies = [Deps::class]
    )
    @PasswordRestorationScope
    interface Component {
        val passwordRestorationViewModel: PasswordRestorationViewModel
    }
}