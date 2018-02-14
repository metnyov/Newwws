package com.wkwn.newwws

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_item.*
import java.util.*


class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        @Suppress("DEPRECATION")
        val date = Date(intent.getStringExtra("publishedAt"))

        val newsItem = NewsItem(intent.getStringExtra("author"), intent.getStringExtra("title"),
                intent.getStringExtra("description"), intent.getStringExtra("url"),
                intent.getStringExtra("urlToImage"), date)

        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        recyclerViewItem.adapter = ItemAdapter(newsItem, this)
    }
}