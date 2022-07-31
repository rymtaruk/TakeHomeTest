package com.fita.test.takehometest

import com.fita.test.core.di.component.CoreComponent
import com.fita.test.core.di.component.CoreComponentProvider
import com.fita.test.core.di.component.DaggerCoreComponent
import com.fita.test.core.di.module.ProviderModule
import com.fita.test.takehometest.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication(), CoreComponentProvider {
    private var coreComponent: CoreComponent? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        ProviderModule.context = applicationContext
        return DaggerAppComponent.builder().application(this).coreComponent(provideCoreComponent())
            .build()
    }

    override fun provideCoreComponent(): CoreComponent {
        if (coreComponent == null) {
            coreComponent = DaggerCoreComponent.builder().build()
        }
        return coreComponent!!
    }
}