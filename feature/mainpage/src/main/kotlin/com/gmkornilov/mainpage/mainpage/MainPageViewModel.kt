package com.gmkornilov.mainpage.mainpage

import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

class MainPageViewModel @Inject constructor(): BaseViewModel<Unit, Unit>() {
    override fun getBaseState() = Unit
}