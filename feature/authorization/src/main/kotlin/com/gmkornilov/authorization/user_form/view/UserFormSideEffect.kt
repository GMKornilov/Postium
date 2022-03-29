package com.gmkornilov.authorization.user_form.view

internal sealed class UserFormSideEffect {
    object UploadPhoto: UserFormSideEffect()

    object ScrollToEnd: UserFormSideEffect()
}