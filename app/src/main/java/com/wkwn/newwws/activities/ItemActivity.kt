package com.wkwn.newwws.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.wkwn.newwws.adapters.ItemAdapter
import com.wkwn.newwws.models.NewsItem
import com.wkwn.newwws.R
import kotlinx.android.synthetic.main.activity_item.*
import java.util.*


class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("DEPRECATION")
        val date = Date(intent.getStringExtra("publishedAt"))

        val newsItem = NewsItem(intent.getStringExtra("author"), intent.getStringExtra("title"),
                intent.getStringExtra("description"), intent.getStringExtra("url"),
                intent.getStringExtra("urlToImage"), date)

        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        recyclerViewItem.adapter = ItemAdapter(newsItem, this)
    }

    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}