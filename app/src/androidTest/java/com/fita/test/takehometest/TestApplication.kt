package com.fita.test.takehometest

import android.app.Application
import com.fita.test.takehometest.di.component.DaggerTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TestApplication : DaggerApplication(){
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestAppComponent.builder().application(this).build()
    }
}