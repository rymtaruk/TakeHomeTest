package com.fita.test.core.di.util

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value:KClass<out ViewModel>)
