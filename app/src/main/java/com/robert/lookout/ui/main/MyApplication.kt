package com.robert.lookout.ui.main

import android.app.Application
import com.robert.lookout.ui.main.di.AppComponent
import com.robert.lookout.ui.main.di.DaggerAppComponent


class MyApplication : Application(){

    val appComponent:AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent():AppComponent{
        return DaggerAppComponent.factory().create(applicationContext)
    }
}

