package com.fita.test.takehometest

import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fita.test.takehometest.server.MockServer
import com.fita.test.takehometest.ui.MainActivity
import com.fita.test.takehometest.ui.MainAdapter
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
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

        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.tvError))
            .check(ViewAssertions.matches(not(isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.rvItems))
            .check(ViewAssertions.matches(isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.rvItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.ViewHolder>(
                0,
                clickOnViewChild(R.id.cvRoot)
            )
        )

        Espresso.onView(ViewMatchers.withId(R.id.cvIndicator)).check(
            ViewAssertions.matches(
                isDisplayed()
            )
        )

        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.rvItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.ViewHolder>(
                1,
                clickOnViewChild(R.id.cvRoot)
            )
        )

        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.rvItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.ViewHolder>(
                2,
                clickOnViewChild(R.id.cvRoot)
            )
        )
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
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById(viewId))
    }
}