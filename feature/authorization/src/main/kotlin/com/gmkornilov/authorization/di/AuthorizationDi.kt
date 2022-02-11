package com.gmkornilov.authorization.di

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.HomeViewModel
import dagger.Component

interface AuthorizationDeps {
    val googleAuthInteractor: GoogleAuthInteractor
    val facebookAuthInteractor: FacebookAuthInteractor
    val emailAuthInteractor: EmailAuthInteractor

    val router: TreeRouter
}

@Component(dependencies = [AuthorizationDeps::class, UserResultHandler::class])
interface AuthorizationComponent {
    val homeViewModel: HomeViewModel
}