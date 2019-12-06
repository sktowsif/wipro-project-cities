package com.example.cities.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.cities.R
import com.example.cities.base.Outcome
import com.example.cities.data.Country
import com.example.cities.ext.addFragment
import com.example.cities.vm.HomeViewModel
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModel<HomeViewModel>()

    private val countryObserver = Observer<Outcome<Country>> {
        if (it is Outcome.Success) supportActionBar?.title = it.data.title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(find(R.id.toolbar))
        supportActionBar?.title = getString(R.string.app_name)

        viewModel.countryLiveData.observe(this, countryObserver)

        if (savedInstanceState == null) {
            addFragment(R.id.home_container, HomeFragment.newInstance())
        }
    }

}