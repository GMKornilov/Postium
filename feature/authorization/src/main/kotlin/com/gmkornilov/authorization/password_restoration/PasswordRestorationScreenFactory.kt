package com.gmkornilov.authorization.password_restoration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.password_restoration.domain.PasswordRestorationFlowEvents
import com.gmkornilov.authorization.password_restoration.view.PasswordRestoration
import com.gmkornilov.authorization.password_restoration.view.PasswordRestorationViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.strings.StringsProvider
import javax.inject.Inject
import javax.inject.Scope

private const val PASSWORD_RESTORATION_KEY = "Password restoration"

internal class PasswordRestorationScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<PasswordRestorationScreenFactory.Deps> {
    private val passwordRestorationHomeScreen = BaseScreen(
        PASSWORD_RESTORATION_KEY,
        onCreate = { _, _ ->
            val component = DaggerPasswordRestorationScreenFactory_Component.builder()
                .deps(dependency)
                .build()
            component.passwordRestorationViewModel
        }
    ) {
        val viewModel = it.get<PasswordRestorationViewModel>()
        PasswordRestoration(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(): Screen<*> {
        return passwordRestorationHomeScreen
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