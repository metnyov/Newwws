package com.wkwn.newwws.activities

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.wkwn.newwws.R
import com.wkwn.newwws.adapters.ViewPagerAdapter
import com.wkwn.newwws.models.UrlApi
import com.wkwn.newwws.fragments.NewsFragment
import com.wkwn.newwws.models.DBHelperSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(){

    private lateinit var country: String
    private lateinit var names: ArrayList<String>
    private var choiceId: Int = 0
    private val countries = UrlApi.Country.values().map { it.name }.toTypedArray()
    private val countriesIds = UrlApi.Country.values().map { it.country }.toTypedArray()
    private val dbHelperSettings = DBHelperSettings(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (Locale.getDefault().language) {
            "ru" -> {
                if (dbHelperSettings.isEmpty())
                    country = UrlApi.Country.Russia.country
                names = arrayListOf("главное", "бизнес", "здоровье", "спорт", "технологии", "шоу-бизнес")
            }
            else -> {
                if (dbHelperSettings.isEmpty())
                    country = UrlApi.Country.UnitedStates.country
                names = arrayListOf("head", "business", "health", "sport", "technology", "entertainment")
            }
        }

        if (dbHelperSettings.isEmpty()){
            val db = dbHelperSettings.writableDatabase
            val contentValues = ContentValues().apply {
                put(DBHelperSettings.KEY_COUNTRY, country)
            }
            db.insert(DBHelperSettings.TABLE_SETTINGS, null, contentValues)
        }
        else {
            country = dbHelperSettings.getCountry()
        }

        choiceId = countriesIds.indexOf(country)

        setSupportActionBar(toolbar)
        viewPager.offscreenPageLimit = 5

        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager) {

        val defaultBundle = Bundle()
        defaultBundle.putString("CATEGORY", UrlApi.Category.DEFAULT.category)
        defaultBundle.putString("COUNTRY", country)

        val businessBundle = Bundle()
        businessBundle.putString("CATEGORY", UrlApi.Category.Business.category)
        businessBundle.putString("COUNTRY", country)

        val healthBundle = Bundle()
        healthBundle.putString("CATEGORY", UrlApi.Category.Health.category)
        healthBundle.putString("COUNTRY", country)

        val sportsBundle = Bundle()
        sportsBundle.putString("CATEGORY", UrlApi.Category.Sports.category)
        sportsBundle.putString("COUNTRY", country)

        val technologyBundle = Bundle()
        technologyBundle.putString("CATEGORY", UrlApi.Category.Technology.category)
        technologyBundle.putString("COUNTRY", country)

        val entertainmentBundle = Bundle()
        entertainmentBundle.putString("CATEGORY", UrlApi.Category.Entertainment.category)
        entertainmentBundle.putString("COUNTRY", country)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        launch(UI) {
            async {
                val pairs = arrayListOf<Pair<Fragment, String>>(
                        Pair(NewsFragment().apply { arguments = defaultBundle }, names[0]),
                        Pair(NewsFragment().apply { arguments = businessBundle }, names[1]),
                        Pair(NewsFragment().apply { arguments = healthBundle }, names[2]),
                        Pair(NewsFragment().apply { arguments = sportsBundle }, names[3]),
                        Pair(NewsFragment().apply { arguments = technologyBundle }, names[4]),
                        Pair(NewsFragment().apply { arguments = entertainmentBundle }, names[5])
                )
                pairs.map { adapter.addFragment(it.first, it.second) }
            }.await()

            viewPager.adapter = adapter
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
            R.id.county -> {
                showAlertChooseCountry()
                true
            }

            else -> super.onOptionsItemSelected(item)
    }

    private fun showAlertChooseCountry() {
        var id = -1
        val alertDialog = AlertDialog.Builder(this@MainActivity)
                .setSingleChoiceItems(countries, choiceId,
                        { _, i ->
                            if (country != countriesIds[i])
                                id = i
                        })
                .setPositiveButton("OK",
                        { _, _ ->
                            if (id != -1 && choiceId != id){
                                country = countriesIds[id]
                                choiceId = id
                                dbHelperSettings.setCountry(country)
                                setupViewPager(viewPager)
                            }
                        })
                .setNeutralButton("CANCEL", { _, _ -> })
                .setTitle(R.string.chooseCountry)
        alertDialog.show()
    }
}