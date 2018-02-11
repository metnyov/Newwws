package com.wkwn.newwws

/*API: https://newsapi.org*/

class UrlApi() {

    private val url: String = "https://newsapi.org/v2/top-headlines?"
    private val apiKey: String = "5ac94b1cfd104e19802d81f30d5c7daa"

    fun create(country:String = "ru") = url + "country=" + country + "&apiKey=" + apiKey
}