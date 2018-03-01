package com.wkwn.newwws.activities

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.wkwn.newwws.AsyncHandler
import com.wkwn.newwws.models.DBHelper
import com.wkwn.newwws.R
import com.wkwn.newwws.models.UrlApi
import com.wkwn.newwws.fragments.NewsFragment
import com.wkwn.newwws.models.DBHelperSettings
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(){

    private lateinit var country: String
    private lateinit var names: ArrayList<String>
    private lateinit var lastMenuItem: MenuItem
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
        else
            country = dbHelperSettings.getCountry()

        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {

        AsyncHandler.SetupViewPagerTask(viewPager, supportFragmentManager).
                execute(Pair(NewsFragment(DBHelper(country, this), UrlApi.Category.DEFAULT, country), names[0]),
                        Pair(NewsFragment(DBHelper(country, this), UrlApi.Category.Business, country), names[1]),
                        Pair(NewsFragment(DBHelper(country, this), UrlApi.Category.Health, country), names[2]),
                        Pair(NewsFragment(DBHelper(country, this), UrlApi.Category.Sports, country), names[3]),
                        Pair(NewsFragment(DBHelper(country, this), UrlApi.Category.Technology, country), names[4]),
                        Pair(NewsFragment(DBHelper(country, this), UrlApi.Category.Entertainment, country), names[5]))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val id = when (country){
            UrlApi.Country.Russia.country -> R.id.ru
            UrlApi.Country.Ukraine.country -> R.id.ua
            UrlApi.Country.UnitedStates.country -> R.id.us
            UrlApi.Country.UnitedKingdom.country -> R.id.gb
            else -> R.id.us
        }
        lastMenuItem = menu.findItem(id)
        lastMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
            R.id.ru -> {
                if (country != UrlApi.Country.Russia.country) {
                    country = UrlApi.Country.Russia.country
                    dbHelperSettings.setCountry(country)
                    lastMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    lastMenuItem = item
                    setupViewPager(viewPager)
                }
                false
            }
            R.id.ua -> {
                if (country != UrlApi.Country.Ukraine.country) {
                    country = UrlApi.Country.Ukraine.country
                    dbHelperSettings.setCountry(country)
                    lastMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    lastMenuItem = item
                    setupViewPager(viewPager)
                }
                false
            }
            R.id.us -> {
                if (country != UrlApi.Country.UnitedStates.country) {
                    country = UrlApi.Country.UnitedStates.country
                    dbHelperSettings.setCountry(country)
                    lastMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    lastMenuItem = item
                    setupViewPager(viewPager)
                }
                false
            }
            R.id.gb -> {
                if (country != UrlApi.Country.UnitedKingdom.country) {
                    country = UrlApi.Country.UnitedKingdom.country
                    dbHelperSettings.setCountry(country)
                    lastMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    lastMenuItem = item
                    setupViewPager(viewPager)
                }
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
}