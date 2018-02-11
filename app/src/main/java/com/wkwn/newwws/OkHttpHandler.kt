package com.wkwn.newwws

import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class OkHttpHandler {
    class RequestTask(recyclerView: RecyclerView) : AsyncTask<String, Void, News.List>() {

        private val rv = recyclerView

        override fun doInBackground(vararg urls: String): News.List {

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(urls[0])
                    .build()
            val response = client.newCall(request).execute()
            val responseText = response.body()!!.string()
            val repos = Gson().fromJson(responseText, News.List::class.java)
            android.util.Log.d("Repos", repos.joinToString { it.title })

            return repos
        }

        override fun onPostExecute(result: News.List) {
            super.onPostExecute(result)
            rv.adapter = ListAdapter(result)
        }
    }
}