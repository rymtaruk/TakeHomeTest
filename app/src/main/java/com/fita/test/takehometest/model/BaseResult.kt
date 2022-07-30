package com.fita.test.takehometest.model

import java.io.Serializable

class BaseResult<T> constructor(var resultCount: Int, var results: T) : Serializable {
}