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
import com.example.cities.ext.reObserve
import com.example.cities.ext.show
import com.example.cities.vm.HomeViewModel
import com.example.cities.widget.LeadingSpan
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_city.view.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by sharedViewModel<HomeViewModel>()

    private val countryObserver = Observer<Outcome<Country>> {
        when (it) {
            is Outcome.Progress -> swipeRefresh.isRefreshing = it.loading
            is Outcome.Failure -> toast(it.e.localizedMessage ?: "Something went wrong")
            is Outcome.Success -> getAdapter().addCities(it.data.cities)
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
        swipeRefresh.setOnRefreshListener(this)

        cityList.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = CityAdapter()
        }

        viewModel.fetchData()
    }

    override fun onRefresh() {
        viewModel.fetchData()
    }

    private fun getAdapter() = cityList.adapter as CityAdapter

    private class CityAdapter(private val cities: ArrayList<City> = arrayListOf()) :
        RecyclerView.Adapter<CityAdapter.ViewHolder>() {

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

                if (city.imageUrl.isNullOrEmpty()) {
                    itemView.imgPhoto.gone()
                    itemView.description.text = city.desc
                } else {
                    itemView.imgPhoto.show()
                    itemView.imgPhoto.load(city.imageUrl)
                    val imageFrameSize = itemView.context.dimen(R.dimen._80sdp)
                    val leftMargin = imageFrameSize + 20
                    val spannedText = SpannableString(city.desc)
                    spannedText.setSpan(LeadingSpan(4, leftMargin), 0, spannedText.length, 0)
                    itemView.description.setText(spannedText, TextView.BufferType.SPANNABLE)
                }
            }
        }

    }

}