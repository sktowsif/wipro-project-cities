package com.example.cities.ui

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.api.load
import com.example.cities.R
import com.example.cities.base.Outcome
import com.example.cities.data.City
import com.example.cities.data.Country
import com.example.cities.ext.gone
import com.example.cities.ext.message
import com.example.cities.ext.reObserve
import com.example.cities.ext.show
import com.example.cities.vm.HomeViewModel
import com.example.cities.widget.LeadingSpan
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_city.view.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var snackBar: Snackbar

    private val viewModel by sharedViewModel<HomeViewModel>()

    private val countryObserver = Observer<Outcome<Country>> {
        when (it) {
            is Outcome.Progress -> swipeRefresh.isRefreshing = it.loading
            is Outcome.Failure -> {
                updateRetry(it.e.message())
                snackBar.setText(it.e.message())
                snackBar.show()
            }
            is Outcome.Success -> {
                if (it.data.cities.isNotEmpty()) updateList(it.data.cities)
                else updateRetry(R.string.err_no_data)
            }
        }
    }

    private val internetConnectivityObserver = Observer<Boolean> { isConnected ->
        if (!isConnected) {
            if (getAdapter().isEmpty()) updateRetry(R.string.err_no_internet)
            snackBar.setText(R.string.err_no_internet)
            snackBar.show()
        } else snackBar.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.connectivityLiveData.reObserve(this, internetConnectivityObserver)
        viewModel.countryLiveData.reObserve(this, countryObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        snackBar =
            Snackbar.make(rootView.cityList, "Something went wrong", Snackbar.LENGTH_INDEFINITE)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh.setOnRefreshListener(this)

        cityList.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = CityAdapter()
        }

        btnRetry.setOnClickListener { viewModel.fetchData() }
    }

    override fun onRefresh() {
        viewModel.fetchData()
    }

    private fun updateList(data: List<City>) {
        errorMessageContainer.gone()
        cityList.show()
        getAdapter().addCities(data)
    }

    private fun updateRetry(messageResId: Int) {
        cityList.gone()
        errorMessageContainer.show()
        lblErrorMsg.setText(messageResId)
    }

    private fun getAdapter() = cityList.adapter as CityAdapter

    private class CityAdapter(private val cities: ArrayList<City> = arrayListOf()) :
        RecyclerView.Adapter<CityAdapter.ViewHolder>() {

        fun isEmpty() = cities.isEmpty()

        fun addCities(newCities: List<City>) {
            cities.clear()
            cities.addAll(newCities)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_city,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bindCity(cities[position])

        override fun getItemCount(): Int = cities.size

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            fun bindCity(city: City) {
                itemView.title.text = city.title
                itemView.description.text = city.desc

                if (!city.imageUrl.isNullOrEmpty()) {
                    itemView.imgPhoto.load(city.imageUrl) {
                        placeholder(R.drawable.img_placeholder)
                        error(R.drawable.img_placeholder)
                    }
                } else itemView.imgPhoto.setImageResource(R.drawable.img_placeholder)

                val imageFrameSize = itemView.context.dimen(R.dimen._80sdp)
                val leftMargin = imageFrameSize + 20
                val spannedText = SpannableString(city.desc)
                spannedText.setSpan(LeadingSpan(4, leftMargin), 0, spannedText.length, 0)
                itemView.description.setText(spannedText, TextView.BufferType.SPANNABLE)

                itemView.setOnClickListener { itemView.context.toast(city.title) }
            }
        }

    }

}