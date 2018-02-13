package com.wkwn.newwws

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import android.graphics.BitmapFactory


class AsyncHandler {
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
    class ImageLoadTask(img: ImageView) : AsyncTask<String?, Void, Bitmap?>() {

        private val image = img

        override fun doInBackground(vararg urls: String?): Bitmap?{
            var mIcon11: Bitmap? = null
            try {
                val stream = java.net.URL(urls[0]).openStream()
                mIcon11 = BitmapFactory.decodeStream(stream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            image.setImageBitmap(result)
        }
    }
}