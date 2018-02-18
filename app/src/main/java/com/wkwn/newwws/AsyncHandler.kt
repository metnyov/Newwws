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


class AsyncHandler {

    @SuppressLint("StaticFieldLeak")
    class RequestTask(private val recyclerView: RecyclerView, private val swipeRefreshLayout: SwipeRefreshLayout,
                      private var news: News, private val dbHelper: DBHelper,
                      private val category: UrlApi.Category, private val context: Context) : AsyncTask<String, Void, News>() {

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
            val curTable = when(category){
                UrlApi.Category.DEFAULT -> DBHelper.TABLE_DEFAULT
                UrlApi.Category.Business -> DBHelper.TABLE_BUSINESS
                UrlApi.Category.Health -> DBHelper.TABLE_HEALTH
                UrlApi.Category.Sports -> DBHelper.TABLE_SPORTS
                UrlApi.Category.Technology -> DBHelper.TABLE_TECHNOLOGY
                UrlApi.Category.Entertainment -> DBHelper.TABLE_ENTERTAINMENT
            }
            news.articles.addAll(result.articles)

            // !!!
            WriteDataBaseTask(dbHelper, news, category).execute()

            recyclerView.adapter = ListAdapter(dbHelper.toNews(curTable), context)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("StaticFieldLeak")
    class WriteDataBaseTask(private val dbHelper: DBHelper, private var news: News,
                        private val category: UrlApi.Category) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg p0: Void?): Void? {

            val curTable = when(category){
                UrlApi.Category.DEFAULT -> DBHelper.TABLE_DEFAULT
                UrlApi.Category.Business -> DBHelper.TABLE_BUSINESS
                UrlApi.Category.Health -> DBHelper.TABLE_HEALTH
                UrlApi.Category.Sports -> DBHelper.TABLE_SPORTS
                UrlApi.Category.Technology -> DBHelper.TABLE_TECHNOLOGY
                UrlApi.Category.Entertainment -> DBHelper.TABLE_ENTERTAINMENT
            }
            if (news.isEmpty())
                return null

            val database = dbHelper.writableDatabase
            val contentValues = ContentValues()

            for (el in news.articles){
                if (dbHelper.checkCompareId(curTable, el.publishedAt.time))
                    break
                contentValues.put(DBHelper.KEY_ID, el.publishedAt.time)
                contentValues.put(DBHelper.KEY_AUTHOR, el.author)
                contentValues.put(DBHelper.KEY_TITLE, el.title)
                contentValues.put(DBHelper.KEY_DESCRIPTION, el.description)
                contentValues.put(DBHelper.KEY_URL, el.url)
                contentValues.put(DBHelper.KEY_PATH_TO_IMAGE, el.urlToImage)
                contentValues.put(DBHelper.KEY_PUBLISHED_AT, el.publishedAt.toString())

                database.insert(curTable, null, contentValues)
            }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ReadDataBaseTask(private val dbHelper: DBHelper, private var news: News,
                        private val category: UrlApi.Category) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg p0: Void?): Void? {
            news = dbHelper.toNews()
            return null
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