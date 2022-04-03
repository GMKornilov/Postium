package com.gmkornilov.root_screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.navigationContainers.AnimatedScreensContainer
import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@ExperimentalAnimationApi
@Composable
internal fun BottomMenuScreen(
    defaultIndex: Int,
    containerRouter: TreeRouter,
    onIndexSelected: (Int) -> Unit,
    bottomNavigationItems: List<BottomNavigationItem>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(bottom = 56.dp)
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
internal fun BottomBar(
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
                selected = isSelected,
                onClick = { onIndexSelected(index) },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}