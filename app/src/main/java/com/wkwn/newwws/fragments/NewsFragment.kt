package com.wkwn.newwws.fragments

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
import com.wkwn.newwws.AsyncHandler
import com.wkwn.newwws.R
import com.wkwn.newwws.adapters.ListAdapter
import com.wkwn.newwws.models.CustomLinearLayoutManager
import com.wkwn.newwws.models.DBHelper
import com.wkwn.newwws.models.UrlApi


class NewsFragment : Fragment() {

    private var count = 20

    private lateinit var rV: RecyclerView
    private lateinit var fabRefresh: FloatingActionButton
    private lateinit var fabUp: FloatingActionButton
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var dbHelper: DBHelper
    private lateinit var category: String
    private lateinit var country: String
    private lateinit var curTable: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = this@NewsFragment.arguments!!.getString("CATEGORY")
        country = this@NewsFragment.arguments!!.getString("COUNTRY")
        curTable = when(category){
            UrlApi.Category.DEFAULT.category -> DBHelper.TABLE_DEFAULT
            UrlApi.Category.Business.category -> DBHelper.TABLE_BUSINESS
            UrlApi.Category.Health.category -> DBHelper.TABLE_HEALTH
            UrlApi.Category.Sports.category -> DBHelper.TABLE_SPORTS
            UrlApi.Category.Technology.category -> DBHelper.TABLE_TECHNOLOGY
            UrlApi.Category.Entertainment.category -> DBHelper.TABLE_ENTERTAINMENT
            else -> DBHelper.TABLE_DEFAULT
        }
        dbHelper = DBHelper(country, activity!!.applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_main, container, false)

        rV = rootView.findViewById(R.id.recyclerView)
        swipe = rootView.findViewById(R.id.swipeRefreshLayout)
        fabRefresh = rootView.findViewById(R.id.fab_refresh)
        fabRefresh.hide()
        fabUp = rootView.findViewById(R.id.fab_up)
        fabUp.hide()

        rV.setHasFixedSize(true)
        rV.layoutManager = CustomLinearLayoutManager(activity!!.applicationContext)
        rV.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                if (dy > 0) {
                    fabUp.show()
                    val pos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    if (pos == count - 1) {
                        count += 20
                        rV.adapter = ListAdapter(dbHelper.toNews(curTable, count), activity!!.applicationContext)
                        rV.scrollToPosition(pos - 1)
                    }
                }
                else
                    fabUp.hide()
            }

        })

        if (!dbHelper.isEmpty(curTable))
            rV.adapter = ListAdapter(dbHelper.toNews(curTable, count), activity!!.applicationContext)
        else {
            if (!hasConnection()) {
                fabRefresh.show()
                Toast.makeText(activity!!.applicationContext, R.string.notConnect, Toast.LENGTH_SHORT).show()
            }
        }
        if (dbHelper.isEmpty() && hasConnection()) {
            AsyncHandler().loadNewsTask(rV, swipe, dbHelper, count, curTable,
                    activity!!.applicationContext, UrlApi().create(category, country))
        }
        else if (hasConnection()) {
            when (getConnectionType()) {
                ConnectivityManager.TYPE_WIFI -> {
                    AsyncHandler().loadNewsTask(rV, swipe, dbHelper, count, curTable,
                            activity!!.applicationContext, UrlApi().create(category, country))
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    AsyncHandler().checkUpdateNewsTask(fabRefresh, dbHelper, curTable,
                            UrlApi().create(category, country, 1))
                }
                else -> {
                    AsyncHandler().loadNewsTask(rV, swipe, dbHelper, count, curTable,
                            activity!!.applicationContext, UrlApi().create(category, country))
                }
            }
        }

        fabUp.setOnClickListener({
            rV.stopScroll()
            rV.smoothScrollToPosition(0)
            fabUp.hide()
        })

        fabRefresh.setOnClickListener({ refreshFragment() })

        swipe.setOnRefreshListener({ refreshFragment() })

        return rootView
    }

    private fun refreshFragment() {
        when {
            hasConnection() -> {
                AsyncHandler().loadNewsTask(rV, swipe, dbHelper, count, curTable,
                        activity!!.applicationContext, UrlApi().create(category, country))
                fabRefresh.hide()
            }

            else -> {
                Toast.makeText(activity!!.applicationContext, R.string.notConnect, Toast.LENGTH_SHORT).show()
                swipe.isRefreshing = false
            }
        }
    }

    private fun hasConnection(): Boolean {
        val cm = activity!!.applicationContext.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    private fun getConnectionType(): Int {
        val cm = activity!!.applicationContext.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo.type
    }
}