package com.gmkornilov.authorization.user_form

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.user_form.domain.UserFormFlowEvents
import com.gmkornilov.authorization.user_form.view.UserForm
import com.gmkornilov.authorization.user_form.view.UserFormViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.user.repository.UserAvatarRepository
import com.gmkornilov.user.repository.UserRepository
import dagger.BindsInstance
import javax.inject.Inject
import javax.inject.Scope

private const val USER_FORM_KEY = "user form"

internal class UserFormScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<UserFormScreenFactory.Deps> {
    private val userFormScreen = BaseScreen(
        USER_FORM_KEY,
        onCreate = { _, arg ->
            val user = arg.get<PostiumUser>()
            val component = DaggerUserFormScreenFactory_Component.builder()
                .deps(dependency)
                .user(user)
                .build()
            component.viewModel
        }
    ) {
        val viewModel = it.get<UserFormViewModel>()

        UserForm(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(): Screen<UserFormViewModel> {
        return userFormScreen
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