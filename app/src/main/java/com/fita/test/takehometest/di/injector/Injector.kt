package com.fita.test.takehometest.di.injector

import android.app.Application
import android.content.Context
import com.fita.test.core.di.component.CoreComponent
import com.fita.test.core.di.component.CoreComponentProvider
import com.fita.test.core.di.component.DaggerCoreComponent
import com.fita.test.core.di.module.ProviderModule
import com.fita.test.takehometest.di.component.AppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class Injector private constructor(private val applicationContext: Application) : HasAndroidInjector, CoreComponentProvider {

//    private val appComponent: AppComponent?
    private var coreComponent: CoreComponent? = null

    @JvmField
    @Inject
    var appInjector: DispatchingAndroidInjector<Any>? = null

    init {
//        appComponent = DaggerAppComponent.builder()
//            .application(applicationContext)
//            .coreComponent(provideCoreComponent())
//            .build()
//        appComponent.inject(this)

    }

    override fun androidInjector(): AndroidInjector<Any> {
        return appInjector!!
    }

    fun getApplicationContext(): Application {
        return applicationContext;
    }

    override fun provideCoreComponent(): CoreComponent {
        if (coreComponent == null) {
            coreComponent = DaggerCoreComponent.builder().build()
        }
        return coreComponent!!
    }

    companion object {
        private var instance: Injector? = null
        private fun initialize(applicationContext: Application) {
            instance = Injector(applicationContext)
            ProviderModule.context = applicationContext.applicationContext
        }


        fun getInstance(context: Context): Injector? {
            if (instance == null) {
                initialize(context.applicationContext as Application)
            }
            return instance
        }
    }
}