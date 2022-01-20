package com.gmkornilov.navigation

import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class BottomNavigationViewModel @Inject constructor(
    val bottomNavigationItems: List<BottomNavigationItem>,
): BaseViewModel<BottomNavigationState, Nothing>() {
    fun onMenuItemClicked(index: Int) = intent {
        val item = bottomNavigationItems[index]
        item.onSelected()

        reduce {
            BottomNavigationState(index, item.router)
        }
    }

    override fun getBaseState(): BottomNavigationState {
        return BottomNavigationState(0, bottomNavigationItems.first().router)
    }
}