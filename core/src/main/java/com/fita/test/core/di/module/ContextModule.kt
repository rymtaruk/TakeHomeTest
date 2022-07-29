package com.fita.test.core.di.module

import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class ContextModule {

    @Binds
    abstract fun bindContext(applicationContext: Context) : Context
}