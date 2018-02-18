package com.wkwn.newwws.activities

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
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    private var country = UrlApi.Country.Russia
    var dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager(viewPager)

        tabs.setupWithViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        AsyncHandler.SetupViewPagerTask(viewPager, supportFragmentManager).
                execute(Pair(NewsFragment(dbHelper, UrlApi.Category.DEFAULT, country), "Главное"),
                        Pair(NewsFragment(dbHelper, UrlApi.Category.Business, country), "Бизнес"),
                        Pair(NewsFragment(dbHelper, UrlApi.Category.Health, country), "Здоровье"),
                        Pair(NewsFragment(dbHelper, UrlApi.Category.Sports, country), "Спорт"),
                        Pair(NewsFragment(dbHelper, UrlApi.Category.Technology, country), "Технологии"),
                        Pair(NewsFragment(dbHelper, UrlApi.Category.Entertainment, country), "Шоу-бизнес"))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
            R.id.ru -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    country = UrlApi.Country.Russia
                    setupViewPager(viewPager)
                    true
                }
                false
            }
            R.id.ua -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    country = UrlApi.Country.Ukraine
                    setupViewPager(viewPager)
                    true
                }
                false
            }
            R.id.us -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    country = UrlApi.Country.UnitedStates
                    setupViewPager(viewPager)
                    true
                }
                false
            }
            R.id.uk -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    country = UrlApi.Country.UnitedKingdom
                    setupViewPager(viewPager)
                    true
                }
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
}