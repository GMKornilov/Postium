package com.gmkornilov.authorization.home.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gmkornilov.authorization.R
import com.gmkornilov.design.commons.buttons.CircularGoogleButton
import com.gmkornilov.design.components.PasswordTextField
import com.gmkornilov.design.theme.PostiumTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@Composable
internal fun Home(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.container.stateFlow.collectAsState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val googleSignInlauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        viewModel::handleGoogleSignInResult
    )

    LaunchedEffect(viewModel) {
        launch {
            viewModel.container.sideEffectFlow.collect {
                when (it) {
                    is HomeSideEffect.GoogleSignIn -> googleSignInlauncher.launch(it.intent)
                    is HomeSideEffect.LoginError -> showError(
                        context.getString(R.string.login_failed_label),
                        scope,
                        snackbarHostState
                    )
                }
            }
        }
    }

    HomeWithState(state = state, homeEvents = viewModel, modifier)
}

private fun showError(
    message: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
    coroutineScope.launch {
        snackbarHostState.showSnackbar(message = message)
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun HomeWithState(state: HomeState, homeEvents: HomeEvents, modifier: Modifier = Modifier) {
    var enteredLogin by remember { mutableStateOf("") }

    var enteredPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 16.dp)
    ) {
        val (
            title, login, password, errorLabel,
            forgotPassword, loginButton, outterLoginLabel,
            outterLoginRow, notRegistered,
        ) = createRefs()

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        OutlinedTextField(
            value = enteredLogin,
            onValueChange = { enteredLogin = it },
            label = { Text(stringResource(R.string.login_hint)) },
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .constrainAs(login) {
                    top.linkTo(title.bottom)
                }
        )

        PasswordTextField(
            value = enteredPassword,
            isPasswordVisible = passwordVisible,
            onPasswordVisibleChange = { passwordVisible = it },
            onValueChange = { enteredPassword = it },
            label = { Text(stringResource(id = R.string.password_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(password) {
                    top.linkTo(login.bottom)
                },
        )

        val text = when (state) {
            HomeState.UserDoesntExist -> stringResource(R.string.unexisting_user_label)
            HomeState.WrongPassword -> stringResource(R.string.wrong_password_label)
            else -> null
        }

        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .constrainAs(errorLabel) {
                        top.linkTo(password.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }

        val bottomBarrier = createBottomBarrier(errorLabel, password)

        TextButton(
            onClick = homeEvents::passwordRestoration,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .constrainAs(forgotPassword) {
                    top.linkTo(bottomBarrier, goneMargin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(text = stringResource(R.string.forgot_password_label))
        }

        Button(
            onClick = { homeEvents.credentialsSignIn(enteredLogin, enteredPassword) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .constrainAs(loginButton) {
                    top.linkTo(forgotPassword.bottom)
                }
        ) {
            if (state is HomeState.Loading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text(text = stringResource(R.string.login_label))
            }
        }

        Text(
            text = stringResource(R.string.auth_extended_label),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(top = 32.dp)
                .constrainAs(outterLoginLabel) {
                    top.linkTo(loginButton.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(outterLoginRow) {
                    top.linkTo(outterLoginLabel.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
//            CircularVkButton(onClick = homeEvents::vkSignIn)

            CircularGoogleButton(onClick = homeEvents::googleSignIn)

//            CircularFacebookButton(onClick = homeEvents::facebookSignIn)
        }

        TextButton(
            onClick = homeEvents::register,
            modifier = Modifier.constrainAs(notRegistered) {
                bottom.linkTo(parent.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.PermIdentity,
                contentDescription = "",
                modifier = Modifier
                    .padding(ButtonDefaults.IconSpacing)
                    .size(ButtonDefaults.IconSize)
            )
            Text(text = stringResource(R.string.not_registered_label))
        }
    }
}

@ExperimentalCoroutinesApi
@Preview
@Composable
internal fun DefaultPreviewHome() {
    PostiumTheme {
        HomeWithState(
            state = HomeState.DEFAULT,
            homeEvents = HomeEvents.MOCK,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@ExperimentalCoroutinesApi
@Preview
@Composable
internal fun LoadingPreviewHome() {
    PostiumTheme {
        HomeWithState(
            state = HomeState.Loading,
            homeEvents = HomeEvents.MOCK,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@ExperimentalCoroutinesApi
@Preview
@Composable
internal fun WrongPasswordPreviewHome() {
    PostiumTheme {
        HomeWithState(
            state = HomeState.WrongPassword,
            homeEvents = HomeEvents.MOCK,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@ExperimentalCoroutinesApi
@Preview
@Composable
internal fun UserDoesntExistPreviewHome() {
    PostiumTheme {
        HomeWithState(
            state = HomeState.UserDoesntExist,
            homeEvents = HomeEvents.MOCK,
            modifier = Modifier.fillMaxSize(),
        )
    }
}