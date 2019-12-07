package com.example.cities.ui

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.example.cities.R
import org.jetbrains.anko.find
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class HomeFragmentTest {

    @Rule @JvmField
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

    @After
    fun tearDown() {
        activity = null
    }

}