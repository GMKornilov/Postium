package com.gmkornilov.root_screen

import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class RootViewModel @Inject constructor(
    val bottomNavigationItems: List<@JvmSuppressWildcards BottomNavigationItem>,
): BaseViewModel<RootState, Nothing>() {
    fun onMenuItemClicked(index: Int) = intent {
        val item = bottomNavigationItems[index]
        item.onSelected()

        reduce {
            RootState(index, item.router)
        }
    }

    override fun getBaseState(): RootState {
        return RootState(0, bottomNavigationItems.first().router)
    }
}