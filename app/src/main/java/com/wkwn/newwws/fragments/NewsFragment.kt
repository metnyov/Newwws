package com.wkwn.newwws.fragments

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wkwn.newwws.*
import com.wkwn.newwws.adapters.ListAdapter
import com.wkwn.newwws.models.DBHelper
import com.wkwn.newwws.models.News
import com.wkwn.newwws.models.UrlApi


@SuppressLint("ValidFragment")
class NewsFragment(private val dbHelper: DBHelper, private val category: UrlApi.Category, private val country: String) : Fragment() {

    private var news: News = News(arrayListOf())
    private lateinit var rV: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var swipe: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView: View = inflater!!.inflate(R.layout.fragment_main, container, false)

        rV = rootView.findViewById(R.id.recyclerView)
        rV.setHasFixedSize(true)
        rV.layoutManager = LinearLayoutManager(activity.applicationContext)
        rV.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            /*TODO: pagination
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
            }*/

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int){
                when(newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if ((recyclerView?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() < 5)
                            fab.hide()
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        if ((recyclerView?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() > 5)
                            fab.show()
                    }
                }
            }
        })

        fab = rootView.findViewById(R.id.fab)
        fab.hide()
        fab.setOnClickListener({
            rV.scrollToPosition(0)
            fab.hide()
        })

        swipe = rootView.findViewById(R.id.swipeRefreshLayout)
        swipe.setOnRefreshListener({ refresh() })

        refresh()

        return rootView
    }

    private fun refresh() {
        swipe.isRefreshing = true

        val curTable = when(category){
            UrlApi.Category.DEFAULT -> DBHelper.TABLE_DEFAULT
            UrlApi.Category.Business -> DBHelper.TABLE_BUSINESS
            UrlApi.Category.Health -> DBHelper.TABLE_HEALTH
            UrlApi.Category.Sports -> DBHelper.TABLE_SPORTS
            UrlApi.Category.Technology -> DBHelper.TABLE_TECHNOLOGY
            UrlApi.Category.Entertainment -> DBHelper.TABLE_ENTERTAINMENT
        }

        if(hasConnection()) {
            when {
                news.isEmpty() -> AsyncHandler.RequestTask(rV, swipe, news, dbHelper, category, activity.applicationContext)
                        .execute(UrlApi().create(category, country))

                dbHelper.checkCompareId(curTable, news.articles[0].publishedAt.time, news.articles[0].url) -> {
                    rV.adapter = ListAdapter(dbHelper.toNews(curTable), activity.applicationContext)
                    swipe.isRefreshing = false
                }

                else -> {
                    if (!news.isEmpty())
                        news.articles.clear()
                    AsyncHandler.RequestTask(rV, swipe, news, dbHelper, category, activity.applicationContext)
                            .execute(UrlApi().create(category, country))
                }
            }
        }
        else {
            rV.adapter = ListAdapter(dbHelper.toNews(curTable), activity.applicationContext)
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