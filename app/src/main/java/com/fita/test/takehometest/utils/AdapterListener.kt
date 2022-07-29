package com.fita.test.takehometest.utils

import android.content.Context

interface AdapterListener<T> {
    fun onClick(context: Context, data: T, position: Int)
}