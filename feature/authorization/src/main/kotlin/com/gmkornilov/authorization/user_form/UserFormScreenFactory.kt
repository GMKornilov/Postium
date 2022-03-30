package com.gmkornilov.authorization.user_form

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.user_form.domain.UserFormFlowEvents
import com.gmkornilov.authorization.user_form.view.UserForm
import com.gmkornilov.authorization.user_form.view.UserFormViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.user.repository.UserAvatarRepository
import com.gmkornilov.user.repository.UserRepository
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val USER_FORM_KEY = "user_form"

internal class UserFormScreenFactory @Inject constructor(
    override val dependency: Deps,
): DependencyProvider<UserFormScreenFactory.Deps> {
    private inner class Factory(
        private val postiumUser: PostiumUser,
    ): ScreenFactory() {
        override fun buildKey(): String {
            return USER_FORM_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerUserFormScreenFactory_Component.builder()
                .deps(dependency)
                .user(postiumUser)
                .build()
            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<UserFormViewModel>()

            UserForm(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }

    fun build(postiumUser: PostiumUser, prevPath: String): Screen<*> {
        return Factory(postiumUser).build(prevPath)
    }

    interface Deps: Dependency {
        val userAvatarRepository: UserAvatarRepository

        val userRepository: UserRepository

        val authInteractor: AuthInteractor

        val userFormFlowEvents: UserFormFlowEvents
    }

    @Scope
    annotation class UserFormScope

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class],
    )
    @UserFormScope
    interface Component {
        val viewModel: UserFormViewModel

        @dagger.Component.Builder
        interface Builder {
            fun deps(deps: Deps): Builder

            @BindsInstance
            fun user(user: PostiumUser): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    interface Module {

    }
}