package com.gmkornilov.navigation.bottom_navigation_items

import com.gmkornilov.navigation.BottomNavigationItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BottomNavigationModule {
    companion object {
        @Provides
        fun bottomNavigationItems(
            homeBottomNavigationItem: HomeBottomNavigationItem,
            profileBottomNavigationItem: ProfileBottomNavigationItem
        ): List<BottomNavigationItem> {
            return listOf(homeBottomNavigationItem, profileBottomNavigationItem)
        }
    }
}