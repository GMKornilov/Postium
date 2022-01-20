package com.gmkornilov.bottom_navigation_items

import android.content.Context
import com.gmkornilov.navigation.BottomNavigationScreenDeps
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BottomNavigationModule {
    companion object {
        @Singleton
        @Provides
        fun profileBottomNavigationsDeps(@ApplicationContext context: Context): ProfileBottomNavigationsDeps {
            return EntryPointAccessors.fromApplication(context, ProfileBottomNavigationsDeps::class.java)
        }

        @Singleton
        @Provides
        fun bottomNavigationScreenDeps(@ApplicationContext context: Context): BottomNavigationScreenDeps {
            return EntryPointAccessors.fromApplication(context, BottomNavigationScreenDeps::class.java)
        }

        @Singleton
        @Provides
        fun bottomNavigationItems(
            homeBottomNavigationItem: HomeBottomNavigationItem,
            profileBottomNavigationItem: ProfileBottomNavigationItem
        ): List<BottomNavigationItem> {
            return listOf(homeBottomNavigationItem, profileBottomNavigationItem)
        }
    }
}