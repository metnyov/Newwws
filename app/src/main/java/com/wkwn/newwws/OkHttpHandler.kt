package com.wkwn.newwws

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


class OkHttpHandler {
    class RequestTask(recyclerView: RecyclerView, cont: Context) : AsyncTask<String, Void, News>() {

        private val rv = recyclerView
        private val context = cont

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
            rv.adapter = ListAdapter(result, context)
        }
    }
}