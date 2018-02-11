package com.wkwn.newwws

/*API: https://newsapi.org*/

class UrlApi {

    private val url: String = "https://newsapi.org/v2/top-headlines"
    private val apiKey: String = "5ac94b1cfd104e19802d81f30d5c7daa"

    enum class Category(val category: String?){
        BUSINESS("business"), ENTERTAINMENT("entertainment"),
        HEALTH("health"), SCIENCE("science"),
        SPORTS("sports"), TECHNOLOGY("technology"), DEFAULT(null);
    }

    fun create(category: Category = Category.DEFAULT, country:String = "ru") =
            if (category == Category.DEFAULT)
                url + "?" +
                "country=" + country +
                "&apiKey=" + apiKey
            else
                url + "?" +
                "country=" + country +
                "&category=" + category +
                "&apiKey=" + apiKey

}