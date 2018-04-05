package com.wkwn.newwws

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import com.wkwn.newwws.adapters.ListAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import android.content.ContentValues
import android.support.design.widget.FloatingActionButton
import android.util.Log
import com.wkwn.newwws.models.DBHelper
import com.wkwn.newwws.models.News
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class AsyncHandler {

    fun loadNewsTask(recyclerView: RecyclerView, swipe: SwipeRefreshLayout, dbHelper: DBHelper,
                     limit: Int, curTable: String, context: Context, url: String) = launch(UI) {

        val news = News(arrayListOf())
        swipe.isRefreshing = true

        async {
            try {
                val client = OkHttpClient()
                        .newBuilder()
                        .build()

                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val responseText = response.body()!!.string()
                news.articles.addAll(Gson().fromJson(responseText, News::class.java).articles)
            } catch (e: Exception) {
                Log.d("Failed connection", e.message)
            }
        }.await()

        if (news.isEmpty() || dbHelper.checkCompare(curTable, news.articles[0].url)) {
            swipe.isRefreshing = false
            recyclerView.adapter = ListAdapter(dbHelper.toNews(curTable, limit), context)
            return@launch
        }

        val database = dbHelper.writableDatabase

        async {
            for (el in news.articles) {

                if (dbHelper.checkCompare(curTable, el.url))
                    break

                val contentValues = ContentValues().apply {
                    put(DBHelper.KEY_AUTHOR, el.author)
                    put(DBHelper.KEY_TITLE, el.title)
                    put(DBHelper.KEY_DESCRIPTION, el.description)
                    put(DBHelper.KEY_URL, el.url)
                    put(DBHelper.KEY_IMAGE_URL, el.urlToImage)
                    put(DBHelper.KEY_DATE, el.publishedAt.time)
                }
                try {
                    database.insert(curTable, null, contentValues)
                } catch (e: Exception) {
                    Log.d("News is duplicate", e.message)
                }
            }
        }.await()

        recyclerView.adapter = ListAdapter(dbHelper.toNews(curTable, limit), context)
        swipe.isRefreshing = false
    }

    fun checkUpdateNewsTask(fab: FloatingActionButton, dbHelper: DBHelper,
                               curTable: String, url: String) = launch(UI) {

        val news = News(arrayListOf())

        async {
            try {
                val client = OkHttpClient()
                        .newBuilder()
                        .build()

                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val responseText = response.body()!!.string()
                news.articles.addAll(Gson().fromJson(responseText, News::class.java).articles)
            } catch (e: Exception) {
                Log.d("Failed connection", e.message)
            }
        }.await()

        if (news.isEmpty() || dbHelper.checkCompare(curTable, news.articles[0].url))
            return@launch
        else
            fab.show()

    }

}