package com.fita.test.takehometest.api

import com.fita.test.takehometest.model.BaseResult
import com.fita.test.takehometest.model.TrackData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/search?music=musicTrack")
    fun doSearchMusic(@Query("term") term: String): Single<BaseResult<MutableList<TrackData>>>
}