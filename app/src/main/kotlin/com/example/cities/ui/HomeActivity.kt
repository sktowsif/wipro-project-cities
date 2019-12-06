package com.example.cities.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cities.R
import com.example.cities.ext.addFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.find

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(find(R.id.toolbar))
        supportActionBar?.title = getString(R.string.app_name)

        if (savedInstanceState == null) {
            addFragment(R.id.home_container, HomeFragment.newInstance())
        }
    }

}