package com.wkwn.newwws.models

/*API: https://newsapi.org*/

class UrlApi {

    private val url: String = "https://newsapi.org/v2/top-headlines"
    private val apiKey: String = "5ac94b1cfd104e19802d81f30d5c7daa"

    enum class Category(val category: String?){
        Business("business"), Entertainment("entertainment"),
        Health("health"), Sports("sports"),
        Technology("technology"), DEFAULT(null);
    }

    enum class Country(val country: String){
        Argentina("ar"), Australia("au"), Austria("at"), Belgium("be"), Brazil("br"), Bulgaria("bg"), Canada("ca"),
        China("cn"), Colombia("co"), Cuba("cu"), CzechRepublic("cz"), Egypt("eg"), France("fr"), Germany("de"),
        Greece("gr"), HongKong("hk"), Hungary("hu"), India("in"), Indonesia("id"), Ireland("ie"), Israel("il"),
        Italy("it"), Japan("jp"), Latvia("lv"), Lithuania("lt"), Malaysia("my"), Mexico("mx"), Morocco("ma"),
        Netherlands("nl"), NewZealand("nz"), Nigeria("ng"), Norway("no"), Philippines("ph"), Poland("pl"), Portugal("pt"),
        Romania("ro"), Russia("ru"), SaudiArabia("sa"), Serbia("rs"), Singapore("sg"), Slovakia("sk"), Slovenia("si"),
        SouthAfrica("za"), SouthKorea("kr"), Sweden("se"), Switzerland("ch"), Taiwan("tw"), Thailand("th"), Turkey("tr"),
        UAE("ae"), Ukraine("ua"), UnitedKingdom("gb"), UnitedStates("us"), Venuzuela("ve");
    }

    fun create(category: Category = Category.DEFAULT, country: Country = Country.Russia) =
            if (category == Category.DEFAULT)
                url + "?" +
                "country=" + country.country +
                "&apiKey=" + apiKey
            else
                url + "?" +
                "country=" + country.country +
                "&category=" + category.category +
                "&apiKey=" + apiKey

}