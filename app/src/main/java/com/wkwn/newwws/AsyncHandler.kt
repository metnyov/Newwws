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
import android.content.ContentValues
import com.wkwn.newwws.models.DBHelper
import com.wkwn.newwws.models.News
import com.wkwn.newwws.models.UrlApi
import java.io.IOException
import java.util.concurrent.TimeUnit


class AsyncHandler {

    @SuppressLint("StaticFieldLeak")
    class RequestTask(private val recyclerView: RecyclerView, private val swipeRefreshLayout: SwipeRefreshLayout,
                      private var news: News, private val dbHelper: DBHelper,
                      category: UrlApi.Category, private val context: Context) : AsyncTask<String, Void, Void>() {

        private val curTable = when(category){
            UrlApi.Category.DEFAULT -> DBHelper.TABLE_DEFAULT
            UrlApi.Category.Business -> DBHelper.TABLE_BUSINESS
            UrlApi.Category.Health -> DBHelper.TABLE_HEALTH
            UrlApi.Category.Sports -> DBHelper.TABLE_SPORTS
            UrlApi.Category.Technology -> DBHelper.TABLE_TECHNOLOGY
            UrlApi.Category.Entertainment -> DBHelper.TABLE_ENTERTAINMENT
        }

        override fun doInBackground(vararg urls: String): Void? {

            try {
                val client = OkHttpClient()
                        .newBuilder()
                        .readTimeout(2000, TimeUnit.MILLISECONDS)
                        .build()

                val request = Request.Builder().url(urls[0]).build()
                val response = client.newCall(request).execute()
                val responseText = response.body()!!.string()
                news.articles.addAll(Gson().fromJson(responseText, News::class.java).articles)
            }
            catch (e: IOException){}

            //--------------- Write Data Base ---------------
            if (news.isEmpty())
                return null

            val database = dbHelper.writableDatabase

            for (el in news.articles){
                if (dbHelper.checkCompareId(curTable, el.publishedAt.time, el.url))
                    break

                val contentValues = ContentValues().apply {
                    put(DBHelper.KEY_ID, el.publishedAt.time)
                    put(DBHelper.KEY_AUTHOR, el.author)
                    put(DBHelper.KEY_TITLE, el.title)
                    put(DBHelper.KEY_DESCRIPTION, el.description)
                    put(DBHelper.KEY_URL, el.url)
                    put(DBHelper.KEY_PATH_TO_IMAGE, el.urlToImage)
                    put(DBHelper.KEY_PUBLISHED_AT, el.publishedAt.toString())
                }

                database.insert(curTable, null, contentValues)
            }
            //----------------------------------------------

            return null
        }

        override fun onPostExecute(result: Void?) {
            recyclerView.adapter = ListAdapter(dbHelper.toNews(curTable), context)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SetupViewPagerTask(private val viewPager: ViewPager,
                             private val fragmentManager: FragmentManager) : AsyncTask<Pair<Fragment, String>, Void, ViewPagerAdapter>() {

        override fun doInBackground(vararg pair: Pair<Fragment, String>): ViewPagerAdapter {
            val adapter = ViewPagerAdapter(fragmentManager)
            for (p in pair)
                adapter.addFragment(p.first, p.second)
            return adapter
        }

        override fun onPostExecute(resultAdapter: ViewPagerAdapter) {
            viewPager.adapter = resultAdapter
        }
    }
}