package com.fita.test.takehometest

import android.content.Intent
import android.view.WindowManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fita.test.takehometest.server.MockServer
import com.fita.test.takehometest.ui.MainActivity
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private val mockServer = MockWebServer()
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        mockServer.start(8088)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
        scenario.close()
    }

    @Test
    fun successCase() {
        mockServer.dispatcher = MockServer.ResponseDispatcher()
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        scenario = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.etSearch))
            .check(ViewAssertions.matches(isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.etSearch)).perform(typeText("Dewa 19"))

        Espresso.onView(ViewMatchers.withId(R.id.tvError))
            .check(ViewAssertions.matches(not(isDisplayed())))

        Thread.sleep(10000)
    }

    @Test
    fun errorCase() {
        mockServer.dispatcher = MockServer.ErrorDispatcher()
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        scenario = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.etSearch)).perform(typeText("123^!*&@#^138"))

        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.tvError))
            .check(ViewAssertions.matches(isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tvError))
            .check(ViewAssertions.matches(withText("No Data")))
    }

    class ToastWatcher : TypeSafeMatcher<Root>() {
        override fun describeTo(description: Description?) {

        }

        override fun matchesSafely(item: Root?): Boolean {
            val type = item!!.windowLayoutParams.get().type
            if (type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY){
                val windowToken = item.decorView.windowToken
                val appToken = item.decorView.applicationWindowToken

                if (windowToken == appToken){
                    return true
                }
            }
            return false
        }
    }
}