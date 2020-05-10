package com.robert.lookout.ui.main.viewModel.main

import com.robert.lookout.ui.main.di.ActivityScope
import com.robert.lookout.ui.main.fragment.HomeFragment
import com.robert.lookout.ui.main.fragment.SearchFragment
import com.robert.lookout.ui.main.viewModel.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create():MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: SearchFragment)
    fun inject(homeFragment: HomeFragment)
}