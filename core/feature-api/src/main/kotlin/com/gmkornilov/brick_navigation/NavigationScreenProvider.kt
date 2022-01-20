package com.gmkornilov.brick_navigation

import com.alphicc.brick.Screen
import com.gmkornilov.view_model.BaseViewModel

interface NavigationScreenProvider<D : BaseViewModel<*, *>> {
    val screen: Screen<D>
}