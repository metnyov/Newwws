package com.wkwn.newwws

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import com.wkwn.newwws.adapters.ListAdapter
import com.wkwn.newwws.adapters.ViewPagerAdapter
import okhttp3.OkHttpClient
import okhttp3.Request


class AsyncHandler {

    @SuppressLint("StaticFieldLeak")
    class RequestTask(private val recyclerView: RecyclerView, private val swipeRefreshLayout: SwipeRefreshLayout,
                      private var news: News, private val context: Context) : AsyncTask<String, Void, News>() {

        override fun doInBackground(vararg urls: String): News {

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(urls[0])
                    .build()
            val response = client.newCall(request).execute()

            val responseText = response.body()!!.string()

            return Gson().fromJson(responseText, News::class.java)
        }

        override fun onPostExecute(result: News) {
            super.onPostExecute(result)
            news.articles.addAll(result.articles)
            recyclerView.adapter = ListAdapter(result, context)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SetupViewPagerTask(private val viewPager: ViewPager,
                             private val fragmentManager: FragmentManager) : AsyncTask<Pair<Fragment, String>, Void, ViewPagerAdapter>() {

        override fun doInBackground(vararg pair: Pair<Fragment, String>): ViewPagerAdapter {
            val adapter = ViewPagerAdapter(fragmentManager)
            for (i: Int in 0..5)
                adapter.addFragment(pair[i].first, pair[i].second)
            return adapter
        }

        override fun onPostExecute(resultAdapter: ViewPagerAdapter) {
            viewPager.adapter = resultAdapter
        }
    }
}