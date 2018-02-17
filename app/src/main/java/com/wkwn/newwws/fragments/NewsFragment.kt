package com.wkwn.newwws.fragments

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wkwn.newwws.AsyncHandler
import com.wkwn.newwws.News
import com.wkwn.newwws.R
import com.wkwn.newwws.UrlApi
import com.wkwn.newwws.adapters.ListAdapter


@SuppressLint("ValidFragment")
class NewsFragment(private val category: UrlApi.Category, private val country: UrlApi.Country) : Fragment() {

    private var news: News = News(arrayListOf())
    private lateinit var rV: RecyclerView
    private lateinit var swipe: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView: View = inflater!!.inflate(R.layout.fragment_main, container, false)

        rV = rootView.findViewById(R.id.recyclerView)
        swipe = rootView.findViewById(R.id.swipeRefreshLayout)

        rV.setHasFixedSize(true)
        rV.layoutManager = LinearLayoutManager(activity.applicationContext)

        if (news.isEmpty())
            refresh()
        else
            rV.adapter = ListAdapter(news, activity.applicationContext)

        swipe.setOnRefreshListener({ refresh() })

        return rootView
    }

    private fun refresh() {
        swipe.isRefreshing = true
        if(hasConnection()) {
            if (!news.isEmpty())
                news.articles.clear()
            AsyncHandler.RequestTask(rV, swipe, news, activity.applicationContext)
                    .execute(UrlApi().create(category, country))
        }
        else {
            //TODO: Взятие данных из БД
            swipe.isRefreshing = false
            Toast.makeText(activity.applicationContext, "Нет соединения", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasConnection(): Boolean {
        val cm = activity.applicationContext.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}