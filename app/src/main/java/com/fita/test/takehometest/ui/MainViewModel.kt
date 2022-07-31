package com.fita.test.takehometest.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fita.test.takehometest.api.repositories.TrackRepository
import com.fita.test.takehometest.model.BaseResult
import com.fita.test.takehometest.model.TrackData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val trackRepository: TrackRepository) : ViewModel() {
    private var disposable: Disposable? = null
    private var responseData: MutableLiveData<MutableList<TrackData>> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var responseMessage: MutableLiveData<String> = MutableLiveData()
    private var isFirstLoad: Boolean = true
    var trackData: MutableList<TrackData> = ArrayList()

    fun getResponseData(): LiveData<MutableList<TrackData>> {
        return responseData
    }

    fun getResponseMessage(): LiveData<String> {
        return responseMessage
    }

    fun isShowLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun loadData(term: String) {
        if (isFirstLoad) {
            isLoading.value = true
            isFirstLoad = false
        }
        disposable = trackRepository.search(term)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<BaseResult<MutableList<TrackData>>>() {
                override fun onSuccess(t: BaseResult<MutableList<TrackData>>) {
                    isLoading.value = false
                    if (t.resultCount > 0) {
                        trackData = t.results
                        responseData.value = t.results
                    } else {
                        responseMessage.value = "No Data"
                    }
                }

                override fun onError(e: Throwable) {
                    isLoading.value = false
                    responseMessage.value = e.localizedMessage
                }

            })
    }

    fun extractPlayerError(code: Int, extras: Int): Boolean {
        when (code) {
            MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                handleExtras(extras)
            }
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                handleExtras(extras)
            }
        }
        return true
    }


    private fun handleExtras(extra: Int) {
        when (extra) {
            MediaPlayer.MEDIA_ERROR_IO -> {
                responseMessage.value = "MEDIA_ERROR_IO"
            }
            MediaPlayer.MEDIA_ERROR_MALFORMED -> {
                responseMessage.value = "MEDIA_ERROR_MALFORMED"
            }
            MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> {
                responseMessage.value = "MEDIA_ERROR_UNSUPPORTED"
            }
            MediaPlayer.MEDIA_ERROR_TIMED_OUT -> {
                responseMessage.value = "MEDIA_ERROR_TIMED_OUT"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable!=null){
            disposable!!.dispose()
        }
    }
}