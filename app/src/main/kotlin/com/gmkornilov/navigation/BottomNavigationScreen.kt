package com.gmkornilov.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.navigationContainers.AnimatedScreensContainer
import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BottomNavigationScreenDeps {
    val router: TreeRouter

    val bottomNavigationItems: List<BottomNavigationItem>
}

@OptIn(ExperimentalAnimationApi::class)
private val bottomNavigationScreen = BaseScreen(
    key = "Bottom navigation",
    onCreate = { _, argument ->
        val deps = argument.get<BottomNavigationScreenDeps>()
        return@BaseScreen BottomNavigationViewModel(deps.bottomNavigationItems)
    },
    content = {
        val viewModel = it.get<BottomNavigationViewModel>()

        val state by viewModel.container.stateFlow.collectAsState()

        val router = state.selectedRouter
        val index = state.selectedIndex

        BottomMenuScreen(
            index,
            router,
            viewModel::onMenuItemClicked,
            viewModel.bottomNavigationItems,
        )
    }
)


class BottomNavigationScreen @Inject constructor() :
    NavigationScreenProvider<BottomNavigationViewModel> {
    override val screen = bottomNavigationScreen
}

@ExperimentalAnimationApi
@Composable
private fun BottomMenuScreen(
    defaultIndex: Int,
    containerRouter: TreeRouter,
    onIndexSelected: (Int) -> Unit,
    bottomNavigationItems: List<BottomNavigationItem>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(bottom = 48.dp)
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            AnimatedScreensContainer(containerRouter)
        }

        BottomBar(
            defaultIndex,
            onIndexSelected,
            bottomNavigationItems,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun BottomBar(
    selectedIndex: Int,
    onIndexSelected: (Int) -> Unit,
    bottomNavigationItems: List<BottomNavigationItem>,
    modifier: Modifier = Modifier
) {
    BottomNavigation(modifier.navigationBarsHeight(additional = 56.dp)) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            val isSelected = index == selectedIndex
            BottomNavigationItem(
                icon = { bottomNavigationItem.IconComposable() },
                label = if (isSelected) {
                    { Text(bottomNavigationItem.title) }
                } else null,
                selected = isSelected,
                onClick = { onIndexSelected(index) },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}