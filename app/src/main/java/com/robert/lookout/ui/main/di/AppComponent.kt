package com.robert.lookout.ui.main.di

import android.content.Context
import com.robert.lookout.ui.main.di.module.AppSubComponents
import com.robert.lookout.ui.main.fragment.HomeFragment
import com.robert.lookout.ui.main.viewModel.main.MainActivityComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubComponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context):AppComponent
    }

    fun mainActivityComponent():MainActivityComponent.Factory
}