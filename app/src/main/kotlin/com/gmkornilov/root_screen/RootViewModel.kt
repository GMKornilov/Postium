package com.gmkornilov.root_screen

import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class RootViewModel @Inject constructor(
    val bottomNavigationItems: List<@JvmSuppressWildcards BottomNavigationItem>,
) : BaseViewModel<RootState, Nothing>() {
    private val routerIndexFlow = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            routerIndexFlow.collect {
                forceUpdateState(RootState(it, getRouter(it)))
            }
        }
    }

    fun onMenuItemClicked(index: Int) = intent {
        val item = bottomNavigationItems[index]
        item.onSelected()

        reduce {
            RootState(index, item.router)
        }
    }

    private fun getRouter(index: Int) = bottomNavigationItems[index].router

    override fun getBaseState(): RootState {
        return RootState(0, bottomNavigationItems.first().router)
    }
}