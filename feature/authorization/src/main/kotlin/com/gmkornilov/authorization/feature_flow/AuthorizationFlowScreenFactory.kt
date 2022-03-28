package com.gmkornilov.authorization.feature_flow

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.HomeScreenFactory
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.authorization.password_restoration.PasswordRestorationScreenFactory
import com.gmkornilov.authorization.password_restoration.domain.PasswordRestorationFlowEvents
import com.gmkornilov.authorization.registration.RegistrationScreenFactory
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.authorization.user_form.UserFormScreenFactory
import com.gmkornilov.authorization.user_form.domain.UserFormFlowEvents
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.strings.StringsProvider
import dagger.Binds
import dagger.BindsInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Scope

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
class AuthorizationFlowScreenFactory @Inject constructor(
    override val dependency: Deps,
) : NavigationScreenProvider<AuthorizationFlowScreenFactory.Deps> {

    fun start(userResultHandler: UserResultHandler, router: TreeRouter) {
        val component = DaggerAuthorizationFlowScreenFactory_Component.builder()
            .deps(dependency)
            .userResultHandler(userResultHandler)
            .router(router)
            .build()

        component.flowInteractor.startAuthorizationFlow()
    }

    interface Deps : Dependency {
        val googleAuthInteractor: GoogleAuthInteractor
        val facebookAuthInteractor: FacebookAuthInteractor
        val emailAuthInteractor: EmailAuthInteractor

        val authInteractor: AuthInteractor

        val stringsProvider: StringsProvider
    }

    @Scope
    annotation class AuthorizationScope

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class],
    )
    @AuthorizationScope
    internal interface Component :
        HomeScreenFactory.Deps,
        RegistrationScreenFactory.Deps,
        UserFormScreenFactory.Deps,
        PasswordRestorationScreenFactory.Deps {
        val flowInteractor: AuthorizationFlowInteractor

        val treeRouter: TreeRouter

        @dagger.Component.Builder
        interface Builder {
            fun deps(deps: Deps): Builder

            @BindsInstance
            fun userResultHandler(userResultHandler: UserResultHandler): Builder

            @BindsInstance
            fun router(treeRouter: TreeRouter): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    internal interface Module {
        @AuthorizationScope
        @Binds
        fun bindHomeDeps(component: Component): HomeScreenFactory.Deps

        @AuthorizationScope
        @Binds
        fun bindRegistrationDeps(component: Component): RegistrationScreenFactory.Deps

        @AuthorizationScope
        @Binds
        fun bindUserFormDeps(component: Component): UserFormScreenFactory.Deps

        @AuthorizationScope
        @Binds
        fun bindPasswordRestorationDeps(component: Component): PasswordRestorationScreenFactory.Deps

        @AuthorizationScope
        @Binds
        fun bindHomeFlowEvents(authorizationFlowInteractor: AuthorizationFlowInteractor): HomeFlowEvents

        @AuthorizationScope
        @Binds
        fun bindRegistrationFlowEvents(authorizationFlowInteractor: AuthorizationFlowInteractor): RegistrationFlowEvents

        @AuthorizationScope
        @Binds
        fun bindUserFormFlowEvents(authorizationFlowInteractor: AuthorizationFlowInteractor): UserFormFlowEvents

        @AuthorizationScope
        @Binds
        fun bindPasswordRestorationFlowEvents(authorizationFlowInteractor: AuthorizationFlowInteractor): PasswordRestorationFlowEvents
    }
}