package com.gmkornilov.authorization.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gmkornilov.authorization.R
import com.gmkornilov.design.buttons.CircularFacebookButton
import com.gmkornilov.design.buttons.CircularGoogleButton
import com.gmkornilov.design.buttons.CircularVkButton
import com.gmkornilov.design.theme.PostiumTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@Composable
fun Home(
    scaffoldState: ScaffoldState,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
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
                    HomeSideEffect.LoginError -> showError(
                        context.getString(R.string.login_failed_label),
                        scope,
                        scaffoldState
                    )
                    is HomeSideEffect.Navigate -> navController.navigate(it.route)
                }
            }
        }
    }

    HomeWithState(state = state, homeEvents = viewModel, modifier)
}

private fun showError(
    message: String,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    coroutineScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(message = message)
    }
}

@Composable
private fun HomeWithState(state: HomeState, homeEvents: HomeEvents, modifier: Modifier = Modifier) {
    var enteredLogin by remember { mutableStateOf("") }

    var enteredPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h1
        )

        OutlinedTextField(
            value = enteredLogin,
            onValueChange = { enteredLogin = it },
            label = { Text(stringResource(R.string.login_hint)) },
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = enteredPassword,
            onValueChange = { enteredPassword = it },
            label = { Text(stringResource(id = R.string.password_hint)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val icon = if (passwordVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.toggle_visibility_content_description)
                    )
                }
            }
        )

        TextButton(
            onClick = homeEvents::passwordRestoration,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        ) {
            Text(text = stringResource(R.string.forgot_password_label))
        }

        Button(
            onClick = { homeEvents.credentialsSignIn(enteredLogin, enteredPassword) },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = stringResource(R.string.login_label))
            }
        }

        Text(
            text = stringResource(R.string.auth_extended_label),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            CircularVkButton(onClick = homeEvents::vkSignIn)

            CircularGoogleButton(onClick = homeEvents::googleSignIn)

            CircularFacebookButton(onClick = homeEvents::facebookSignIn)
        }

        TextButton(
            onClick = homeEvents::register,
            modifier = Modifier.padding(top = 64.dp)
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

@Preview
@Composable
fun DefaultPreviewHome() {
    PostiumTheme {
        HomeWithState(
            state = HomeState.DEFAULT,
            homeEvents = HomeEvents.MOCK
        )
    }
}