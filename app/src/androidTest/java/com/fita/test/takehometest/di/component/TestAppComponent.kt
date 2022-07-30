package com.fita.test.takehometest.di.component

import com.fita.test.core.di.module.ContextModule
import com.fita.test.core.di.util.AppScope
import com.fita.test.takehometest.TestApplication
import com.fita.test.takehometest.di.module.ActivityModule
import com.fita.test.takehometest.di.module.TestApiModule
import com.fita.test.takehometest.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@AppScope
@Component(
    modules = [TestApiModule::class, ContextModule::class, ActivityModule::class, ViewModelModule::class, AndroidInjectionModule::class]
)
interface TestAppComponent : AndroidInjector<TestApplication> {
    @Component.Builder
    interface Build {
        @BindsInstance
        fun application(application: TestApplication) : Build
        fun build() : TestAppComponent
    }
}