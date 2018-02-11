package com.wkwn.newwws

import java.util.*


class NewsItem {
    var author: String = ""
    var title: String = ""
    var description: String = ""
    var url: String = ""
    var urlToImage: String = ""
    var publishedAt: Date = Date()
}

data class News(val articles: ArrayList<NewsItem>)
