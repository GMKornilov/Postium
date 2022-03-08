package com.gmkornilov.authorization.registration.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.authorization.R
import com.gmkornilov.design.components.PasswordTextField
import com.gmkornilov.design.theme.PostiumTheme

@Composable
internal fun Registration(
    registrationViewModel: RegistrationViewModel,
    modifier: Modifier = Modifier,
) {
    val state by registrationViewModel.container.stateFlow.collectAsState()

    RegistrationWithState(
        state = state,
        registrationEvents = registrationViewModel,
        modifier = modifier,
    )
}

@Composable
private fun RegistrationWithState(
    state: RegistrationState,
    registrationEvents: RegistrationEvents,
    modifier: Modifier = Modifier,
) {
    var enteredLogin by remember { mutableStateOf("") }

    var enteredPassword by remember { mutableStateOf("") }
    var enteredPasswordVisible by remember { mutableStateOf(false) }

    var enteredPasswordConfirmation by remember { mutableStateOf("") }
    var enteredPasswordConfirmationVisible by remember { mutableStateOf(false) }

    val errorColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colors.error,
        unfocusedBorderColor = MaterialTheme.colors.error,
        focusedLabelColor = MaterialTheme.colors.error,
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = enteredLogin,
            onValueChange = { enteredLogin = it },
            label = { Text(stringResource(R.string.login_hint)) },
            modifier = Modifier.fillMaxWidth(),
            colors = if (state.emailError) {
                errorColors
            } else TextFieldDefaults.outlinedTextFieldColors(),
        )

        PasswordTextField(
            value = enteredPassword,
            isPasswordVisible = enteredPasswordVisible,
            onPasswordVisibleChange = { enteredPasswordVisible = it },
            onValueChange = { enteredPassword = it },
            label = { Text(stringResource(id = R.string.password_hint)) },
            modifier = Modifier.fillMaxWidth(),
            colors = if (state.passwordError) {
                errorColors
            } else null,
        )

        PasswordTextField(
            value = enteredPasswordConfirmation,
            isPasswordVisible = enteredPasswordConfirmationVisible,
            onPasswordVisibleChange = { enteredPasswordConfirmationVisible = it },
            onValueChange = { enteredPasswordConfirmation = it },
            label = { Text(stringResource(id = R.string.password_confirmation_hint)) },
            modifier = Modifier.fillMaxWidth(),
            colors = if (state.passwordConfirmationError) {
                errorColors
            } else null,
        )

        if (state.errorLabel != null) {
            Text(
                text = state.errorLabel,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Button(
            onClick = {
                registrationEvents.registerUser(
                    enteredLogin,
                    enteredPassword,
                    enteredPasswordConfirmation
                )
            },
            modifier = Modifier
                .padding(top = 32.dp)
                .height(48.dp)
                .fillMaxWidth()
        ) {
            if (state.loading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text(text = stringResource(id = R.string.register))
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewRegistration() {
    PostiumTheme {
        RegistrationWithState(
            state = RegistrationState.DEFAULT,
            registrationEvents = RegistrationEvents.MOCK,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
internal fun PreviewRegistrationPasswordDontMatch() {
    PostiumTheme {
        RegistrationWithState(
            state = RegistrationState(
                emailError = true,
                passwordError = true,
                passwordConfirmationError = true,
                errorLabel = stringResource(id = R.string.password_dont_match)
            ),
            registrationEvents = RegistrationEvents.MOCK,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
internal fun PreviewRegistrationLoading() {
    PostiumTheme {
        RegistrationWithState(
            state = RegistrationState(
                loading = true,
            ),
            registrationEvents = RegistrationEvents.MOCK,
            modifier = Modifier.fillMaxSize(),
        )
    }
}