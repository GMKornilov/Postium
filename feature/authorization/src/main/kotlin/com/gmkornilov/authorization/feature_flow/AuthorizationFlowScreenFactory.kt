package com.gmkornilov.authorization.feature_flow

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.HomeScreenFactory
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.authorization.registration.RegistrationScreenFactory
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
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
    }

    @Scope
    annotation class AuthorizationScope

    @AuthorizationScope
    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class],
    )
    interface Component : HomeScreenFactory.Deps, RegistrationScreenFactory.Deps {
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
    interface Module {
        @AuthorizationScope
        @Binds
        fun bindHomeDeps(component: Component): HomeScreenFactory.Deps

        @AuthorizationScope
        @Binds
        fun bindRegistrationDeps(component: Component): RegistrationScreenFactory.Deps

        @AuthorizationScope
        @Binds
        fun bindHomeFlowEvents(authorizationFlowInteractor: AuthorizationFlowInteractor): HomeFlowEvents
    }
}