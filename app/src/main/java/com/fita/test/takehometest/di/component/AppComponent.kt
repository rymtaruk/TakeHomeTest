package com.fita.test.takehometest.di.component

import android.app.Application
import com.fita.test.core.di.component.CoreComponent
import com.fita.test.core.di.module.ContextModule
import com.fita.test.core.di.util.AppScope
import com.fita.test.takehometest.di.injector.Injector
import com.fita.test.takehometest.di.module.ActivityModule
import com.fita.test.takehometest.di.module.ApiModule
import com.fita.test.takehometest.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@AppScope
@Component(
    modules = [ApiModule::class, ContextModule::class, ActivityModule::class, ViewModelModule::class, AndroidInjectionModule::class],
    dependencies = [CoreComponent::class]
)
interface AppComponent {

    fun inject(injector: Injector)

    @Component.Builder
    interface Build {
        @BindsInstance
        fun application(application: Application) : Build
        fun coreComponent(component: CoreComponent) : Build
        fun build() : AppComponent
    }
}