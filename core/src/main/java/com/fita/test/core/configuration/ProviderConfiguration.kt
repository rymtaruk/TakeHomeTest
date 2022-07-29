package com.fita.test.core.configuration

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fita.test.core.BuildConfig
import okhttp3.OkHttpClient

class ProviderConfiguration {

    companion object {
        fun getClient(applicationContext: Context?): OkHttpClient.Builder {
            val builder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG){
                val chuckInterceptor = ChuckerInterceptor.Builder(applicationContext!!).build()
                builder.addInterceptor(chuckInterceptor)
            }

            return builder
        }
    }
}