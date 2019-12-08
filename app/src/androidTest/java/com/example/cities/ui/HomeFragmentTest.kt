package com.example.cities.ui

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.example.cities.R
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeFragmentTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java)

    private var activity: HomeActivity? = null

    @Before
    fun setUp() {
        activity = activityTestRule.activity
    }

    @Test
    fun testLaunch() {

        assertNotNull(activity)

        // Create container for fragment inside a activity
        val container = activity?.findViewById<FrameLayout>(R.id.home_container)
        assertNotNull(container)

        val fragment = HomeFragment.newInstance()

        // load the fragment
        activity!!.supportFragmentManager.beginTransaction()
            .add(container!!.id, fragment)
            .commit()

        getInstrumentation().waitForIdleSync()

        // Find a view inside the fragment
        val view = fragment.view?.findViewById<RecyclerView>(R.id.cityList)
        // If view is not null, fragment is loaded
        assertNotNull(view)
    }

    @Test
    fun testRecyclerViewWithData() {
        // Wait for api call to finish
        Thread.sleep(5000)
        // Check data loaded with item click toast message
        Espresso.onView(ViewMatchers.withId(R.id.cityList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    ViewActions.click()
                )
            )
    }

    @After
    fun tearDown() {
        activity = null
    }

}