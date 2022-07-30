package com.fita.test.takehometest.ui

import com.fita.test.takehometest.api.repositories.TrackRepository
import com.fita.test.takehometest.model.BaseResult
import com.fita.test.takehometest.model.TrackData
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*
import kotlin.collections.ArrayList

class MainViewModelTest {

    @Mock
    private lateinit var mockRepository: TrackRepository

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(mockRepository)
    }

    @Test
    fun showDataFromApi() {
        Mockito.`when`(mockRepository.search("Marley")).thenReturn(Single.just(dummyData()))

        val testObserver = TestObserver<BaseResult<MutableList<TrackData>>>()
        mockRepository.search("Marley").subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.awaitTerminalEvent()
        val expected : String = Gson().toJson(dummyData())
        testObserver.assertValue {
            Gson().toJson(it).equals(expected)
        }
    }

    private fun dummyData(): BaseResult<MutableList<TrackData>> {
        val items: MutableList<TrackData> = ArrayList()
        items.add(
            TrackData(
                "Dewa 19",
                "The Best of Dewa 19",
                "Kangen",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/2a/2c/00/2a2c00ef-d8c8-41da-6999-b504c3f0e4a0/mzaf_1875372919675949161.plus.aac.p.m4a",
                "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3b/ec/30/3bec30fc-5686-1c00-6829-31970f967fd0/Dewa19.jpg/100x100bb.jpg"
            )
        )

        return BaseResult(1, items)
    }
}