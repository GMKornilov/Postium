package com.gmkornilov.authorization.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.authorization.registration.view.Registration
import com.gmkornilov.authorization.registration.view.RegistrationViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import javax.inject.Inject
import javax.inject.Scope

private const val REGISTRATION_KEY = "registration"

internal class RegistrationScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<RegistrationScreenFactory.Deps> {
    private val registrationScreen = BaseScreen(
        REGISTRATION_KEY,
        onCreate = { _, _ ->
            val component = DaggerRegistrationScreenFactory_Component.builder()
                .deps(dependency)
                .build()

            component.registrationViewModel
        }
    ) {
        val viewModel = it.get<RegistrationViewModel>()

        Registration(viewModel, Modifier.fillMaxSize())
    }

    fun build(): Screen<RegistrationViewModel> {
        return registrationScreen
    }

    interface Deps: Dependency {
        val registrationFlowEvents: RegistrationFlowEvents
    }

    @Scope
    annotation class RegistrationScope

    @RegistrationScope
    @dagger.Component(dependencies = [Deps::class], modules = [Module::class])
    interface Component {
        val registrationViewModel: RegistrationViewModel
    }

    @dagger.Module
    interface Module {

    }
}