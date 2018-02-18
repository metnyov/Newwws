package com.wkwn.newwws.models

import java.util.*


class NewsItem(val author: String?, val title: String, val description: String?,
               val url: String?, val urlToImage: String?, val publishedAt: Date){

    fun getFormattedDateString(pattern: String = "HH:mm") = android.text.format.DateFormat.format(pattern, publishedAt).toString()
}

data class News(val articles: ArrayList<NewsItem>){
    fun isEmpty() = articles.isEmpty()
}