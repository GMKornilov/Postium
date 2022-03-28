package com.gmkornilov.authorization.password_restoration.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.authorization.R
import com.gmkornilov.design.theme.PostiumTheme

@Composable
internal fun PasswordRestoration(
    viewModel: PasswordRestorationViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    PasswordRestorationWithState(
        state = state,
        passwordRestorationEvents = viewModel,
        modifier = modifier
    )
}

@Composable
private fun PasswordRestorationWithState(
    state: PasswordRestorationState,
    passwordRestorationEvents: PasswordRestorationEvents,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is PasswordRestorationState.EnterEmail -> EnterEmailState(
            state,
            passwordRestorationEvents = passwordRestorationEvents,
            modifier = modifier.background(MaterialTheme.colors.surface)
        )
        PasswordRestorationState.EmailSend -> EmailSendState(
            passwordRestorationEvents = passwordRestorationEvents,
            modifier = modifier.background(MaterialTheme.colors.surface),
        )
    }
}

@Composable
private fun EnterEmailState(
    state: PasswordRestorationState.EnterEmail,
    passwordRestorationEvents: PasswordRestorationEvents,
    modifier: Modifier = Modifier,
) {
    var enteredEmail by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = enteredEmail,
            onValueChange = { enteredEmail = it },
            label = { Text(stringResource(R.string.enter_email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
        )

        state.errorMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.error,
            )
        }

        Button(
            onClick = { passwordRestorationEvents.sendRestorationEmail(enteredEmail) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp)
                .height(48.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text(stringResource(R.string.send_email))
            }
        }
    }
}

@Composable
private fun EmailSendState(
    passwordRestorationEvents: PasswordRestorationEvents,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.email_send),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.button
        )

        Button(
            onClick = { passwordRestorationEvents.backToLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp)
                .height(48.dp)
        ) {
            Text(stringResource(R.string.back))
        }
    }
}

@Preview
@Composable
private fun EnterEmailPreview() {
    StatePreview(state = PasswordRestorationState.EnterEmail())
}

@Preview
@Composable
private fun EnterEmailLoadingPreview() {
    StatePreview(state = PasswordRestorationState.EnterEmail(isLoading = true))
}

@Preview
@Composable
private fun EnterEmailErrorPreview() {
    StatePreview(
        state = PasswordRestorationState.EnterEmail(
            isLoading = true,
            errorMessage = "test"
        )
    )
}

@Preview
@Composable
private fun EmailSendPreview() {
    StatePreview(state = PasswordRestorationState.EmailSend)
}

@Composable
private fun StatePreview(state: PasswordRestorationState) {
    PostiumTheme {
        PasswordRestorationWithState(
            state = state,
            passwordRestorationEvents = PasswordRestorationEvents.MOCK,
            modifier = Modifier.fillMaxSize()
        )
    }
}