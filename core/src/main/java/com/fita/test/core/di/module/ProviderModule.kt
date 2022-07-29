package com.fita.test.core.di.module

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Keep
import com.fita.test.core.configuration.ProviderConfiguration
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Keep
@Module
class ProviderModule {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    @Singleton
    @Provides
    fun provideConfigRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(ProviderConfiguration.getClient(getContext()).build())
            .build()
    }

    private fun getContext(): Context? {
        return context
    }
}