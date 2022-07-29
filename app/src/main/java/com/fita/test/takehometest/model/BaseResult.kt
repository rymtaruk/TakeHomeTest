package com.fita.test.takehometest.model

import java.io.Serializable

class BaseResult<T> : Serializable {
    var resultCount: Int = 0
    var results: T? = null;
}