package com.fita.test.takehometest.api.repositories

import com.fita.test.takehometest.api.Api
import com.fita.test.takehometest.model.BaseResult
import com.fita.test.takehometest.model.TrackData
import io.reactivex.Single
import javax.inject.Inject

class TrackRepository @Inject constructor(private val api: Api) {
    fun search(term: String): Single<BaseResult<MutableList<TrackData>>> {
        return api.doSearchMusic(term)
    }
}