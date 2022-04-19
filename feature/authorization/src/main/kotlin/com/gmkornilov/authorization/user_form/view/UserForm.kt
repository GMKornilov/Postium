package com.gmkornilov.authorization.user_form.view

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.authorization.R
import com.gmkornilov.design.components.LocalAvatarSize
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.theme.PostiumTheme
import com.google.accompanist.pager.*
import compose.icons.TablerIcons
import compose.icons.tablericons.CameraOff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, InternalCoroutinesApi::class)
@Composable
internal fun UserForm(
    viewModel: UserFormViewModel,
    modifier: Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    BackHandler {
        val index = pagerState.currentPage
        if (index == 0) {
            viewModel.formFinished()
        } else {
            scope.launch { pagerState.animateScrollToPage(index - 1) }
        }
    }

    val selectImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            viewModel.photoUploaded(it)
        }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                UserFormSideEffect.UploadPhoto -> selectImageLauncher.launch("image/*")
                UserFormSideEffect.ScrollToEnd -> pagerState.animateScrollToPage(UserFormStep.values().lastIndex)
            }
        }
    }

    UserFormWithState(
        state = state,
        userFormEvents = viewModel,
        scope = scope,
        pagerState = pagerState,
        modifier = modifier,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun UserFormWithState(
    state: UserFormState,
    userFormEvents: UserFormEvents,
    scope: CoroutineScope,
    pagerState: PagerState,
    modifier: Modifier,
) {
    val steps = UserFormStep.values()

    val index = pagerState.currentPage
    val isLast = index == steps.lastIndex

    Box(modifier = modifier) {
        HorizontalPager(
            count = steps.size, state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colors.surface
                )
        ) { page ->
            when (steps[page]) {
                UserFormStep.GREETING -> GreetingPage()
                UserFormStep.PHOTO -> PhotoUploadPage(state, userFormEvents)
                UserFormStep.USERNAME -> UserNamePage(state, userFormEvents)
                UserFormStep.FINAL -> FinalPage(userFormEvents)
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )

        if (!isLast) {
            FloatingActionButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(index + 1) }
                },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(Icons.Outlined.NavigateNext, null)
            }
        }
    }
}

@Composable
private fun GreetingPage() {
    Text(
        stringResource(R.string.user_form_greeting),
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.h5,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun PhotoUploadPage(
    state: UserFormState,
    userFormEvents: UserFormEvents,
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            stringResource(R.string.photo_upload_label),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.size(32.dp))

        CompositionLocalProvider(
            LocalAvatarSize provides 128.dp
        ) {
            if (state.avatarUrl != null) {
                UserAvatar(avatarUrl = state.avatarUrl)
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(LocalAvatarSize.current)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colors.onSurface, CircleShape),
                ) {
                    Icon(
                        imageVector = TablerIcons.CameraOff,
                        contentDescription = null,
                        modifier = Modifier.size(LocalAvatarSize.current * 2 / 3)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(4.dp))

        Button(onClick = { userFormEvents.photoUploadedClicked() }) {
            Text(
                text = stringResource(id = R.string.photo_upload_button).toUpperCase(Locale.current),
            )
        }
    }
}

@Composable
private fun UserNamePage(
    state: UserFormState,
    userFormEvents: UserFormEvents
) {
    var enteredUsername by remember { mutableStateOf(state.username) }

    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            stringResource(R.string.username_header),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.size(32.dp))

        OutlinedTextField(
            value = enteredUsername,
            onValueChange = { enteredUsername = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        Button(
            onClick = { userFormEvents.updateUsername(enteredUsername) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(start = 32.dp, end = 32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.username_button).toUpperCase(Locale.current),
            )
        }
    }
}

@Composable
private fun FinalPage(
    userFormEvents: UserFormEvents
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            stringResource(R.string.userform_finished),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.size(32.dp))

        Button(
            onClick = { userFormEvents.formFinished() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(start = 32.dp, end = 32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.userform_finished_button).toUpperCase(Locale.current),
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun UserFormFirstPage() {
    PostiumTheme {
        UserFormWithState(
            state = UserFormState(),
            modifier = Modifier.fillMaxSize(),
            scope = rememberCoroutineScope(),
            pagerState = rememberPagerState(0),
            userFormEvents = UserFormEvents.MOCK
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun UserFormSecondPage() {
    PostiumTheme {
        UserFormWithState(
            state = UserFormState(),
            modifier = Modifier.fillMaxSize(),
            scope = rememberCoroutineScope(),
            pagerState = rememberPagerState(1),
            userFormEvents = UserFormEvents.MOCK
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun UserFormThirdPage() {
    PostiumTheme {
        UserFormWithState(
            state = UserFormState(),
            modifier = Modifier.fillMaxSize(),
            scope = rememberCoroutineScope(),
            pagerState = rememberPagerState(2),
            userFormEvents = UserFormEvents.MOCK,
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun UserFormFourthPage() {
    PostiumTheme {
        UserFormWithState(
            state = UserFormState(),
            modifier = Modifier.fillMaxSize(),
            scope = rememberCoroutineScope(),
            pagerState = rememberPagerState(3),
            userFormEvents = UserFormEvents.MOCK,
        )
    }
}