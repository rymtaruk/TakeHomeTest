package com.fita.test.core.di.util

import androidx.annotation.Keep
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.*

@Keep
@Scope
@Retention(value = RUNTIME)
annotation class AppScope