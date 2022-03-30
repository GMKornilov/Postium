package com.gmkornilov.authorization.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.authorization.registration.view.Registration
import com.gmkornilov.authorization.registration.view.RegistrationViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.strings.StringsProvider
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val REGISTRATION_KEY = "registration"

internal class RegistrationScreenFactory @Inject constructor(
    override val dependency: Deps,
): DependencyProvider<RegistrationScreenFactory.Deps> {
    private inner class Factory: ScreenFactory() {
        override fun buildKey(): String {
            return REGISTRATION_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerRegistrationScreenFactory_Component.builder()
                .deps(dependency)
                .build()

            return component.registrationViewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<RegistrationViewModel>()
            Registration(viewModel, Modifier.fillMaxSize())
        }
    }

    fun build(prevPath: String): Screen<*> {
        return Factory().build(prevPath)
    }

    interface Deps: Dependency {
        val registrationFlowEvents: RegistrationFlowEvents

        val emailAuthInteractor: EmailAuthInteractor

        val stringsProvider: StringsProvider
    }

    @Scope
    annotation class RegistrationScope

    @dagger.Component(dependencies = [Deps::class], modules = [Module::class])
    @RegistrationScope
    interface Component {
        val registrationViewModel: RegistrationViewModel
    }

    @dagger.Module
    interface Module {

    }
}