package com.fita.test.core.di.component

import androidx.annotation.Keep
import com.fita.test.core.di.module.ContextModule
import com.fita.test.core.di.module.ProviderModule
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Keep
@Singleton
@Component(modules = [ProviderModule::class, ContextModule::class])
interface CoreComponent {
    fun provideRetrofit() : Retrofit
}