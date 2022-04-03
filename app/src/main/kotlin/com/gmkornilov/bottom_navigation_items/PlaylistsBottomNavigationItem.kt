package com.gmkornilov.bottom_navigation_items

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alphicc.brick.TreeRouter
import com.gmkornilov.postium.R
import com.gmkornilov.root_screen.ROOT_KEY
import compose.icons.TablerIcons
import compose.icons.tablericons.Bookmarks
import javax.inject.Inject

class PlaylistsBottomNavigationItem @Inject constructor(
    rootRouter: TreeRouter,
): BottomNavigationItem {
    @Composable
    override fun IconComposable() {
        Icon(TablerIcons.Bookmarks, null)
    }

    @Composable
    override fun TitleComposable() {
        Text(stringResource(R.string.playlists_tab))
    }

    override val router: TreeRouter = rootRouter.branch(ROOT_KEY)
}