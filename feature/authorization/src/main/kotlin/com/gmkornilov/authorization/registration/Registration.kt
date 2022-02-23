package com.gmkornilov.authorization.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.authorization.R
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.design.components.PasswordTextField
import com.gmkornilov.design.theme.PostiumTheme

private const val REGISTRATION_KEY = "registration"

val registrationScreen = BaseScreen(
    REGISTRATION_KEY,
    onCreate = { _, _ -> RegistrationViewModel() }
) {
    val viewModel = it.get<RegistrationViewModel>()

    Registration(viewModel)
}

@Composable
internal fun Registration(
    registrationViewModel: RegistrationViewModel,
    modifier: Modifier = Modifier,
) {
    val state by registrationViewModel.container.stateFlow.collectAsState()

    RegistrationWithState(
        state = state,
        registrationEvents = registrationViewModel
    )
}

@Composable
internal fun RegistrationWithState(
    state: RegistrationState,
    registrationEvents: RegistrationEvents,
    modifier: Modifier = Modifier,
) {
    var enteredLogin by remember { mutableStateOf("") }

    var enteredPassword by remember { mutableStateOf("") }
    var enteredPasswordVisible by remember { mutableStateOf(false) }

    var enteredPasswordConfirmation by remember { mutableStateOf("") }
    var enteredPasswordConfirmationVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = enteredLogin,
            onValueChange = { enteredLogin = it },
            label = { Text(stringResource(R.string.login_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        PasswordTextField(
            value = enteredPassword,
            isPasswordVisible = enteredPasswordVisible,
            onPasswordVisibleChange = { enteredPasswordVisible = it },
            onValueChange = { enteredPassword = it },
            label = { Text(stringResource(id = R.string.password_hint)) },
            modifier = Modifier.fillMaxWidth(),
        )

        PasswordTextField(
            value = enteredPasswordConfirmation,
            isPasswordVisible = enteredPasswordConfirmationVisible,
            onPasswordVisibleChange = { enteredPasswordConfirmationVisible = it },
            onValueChange = { enteredPasswordConfirmation = it },
            label = { Text(stringResource(id = R.string.password_confirmation_hint)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {  },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(text = stringResource(id = R.string.register))
        }
    }
}

@Preview
@Composable
internal fun PreviewRegistration() {
    PostiumTheme {
        RegistrationWithState(
            state = RegistrationState.None,
            registrationEvents = RegistrationEvents.MOCK
        )
    }
}