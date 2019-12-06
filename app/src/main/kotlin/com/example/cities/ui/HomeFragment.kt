package com.example.cities.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.cities.R
import com.example.cities.base.Outcome
import com.example.cities.data.Country
import com.example.cities.ext.reObserve
import com.example.cities.vm.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by sharedViewModel<HomeViewModel>()

    private val countryObserver = Observer<Outcome<Country>> {
        when (it) {
            is Outcome.Progress -> swipeRefresh.isRefreshing = it.loading
            is Outcome.Failure -> toast(it.e.localizedMessage ?: "Something went wrong")
            is Outcome.Success -> toast(it.data.title)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.countryLiveData.reObserve(this, countryObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData()
    }

}