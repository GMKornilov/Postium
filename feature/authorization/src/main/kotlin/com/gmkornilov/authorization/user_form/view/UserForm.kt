package com.gmkornilov.authorization.user_form.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.theme.PostiumTheme
import com.google.accompanist.pager.*

private const val PAGE_COUNT = 2

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun UserForm(
    viewModel: UserFormViewModel,
    modifier: Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    val pagerState = rememberPagerState()

    UserFormWithState(
        state = state,
        pagerState = pagerState,
        modifier = modifier,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun UserFormWithState(
    state: UserFormState,
    pagerState: PagerState,
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        HorizontalPager(
            count = PAGE_COUNT, state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colors.surface
                )
        ) { page ->
            when (page) {
                0 -> FirstPage()
                1 -> SecondPage()
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun FirstPage() {
    Text("First page")
}

@Composable
private fun SecondPage() {
    Text("Second page")
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun UserFormFirstPage() {
    PostiumTheme {
        UserFormWithState(
            state = UserFormState(),
            modifier = Modifier.fillMaxSize(),
            pagerState = rememberPagerState(0)
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
            pagerState = rememberPagerState(1)
        )
    }
}