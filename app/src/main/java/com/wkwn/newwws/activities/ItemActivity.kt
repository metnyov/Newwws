package com.wkwn.newwws.activities

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wkwn.newwws.models.NewsItem
import com.wkwn.newwws.R
import com.wkwn.newwws.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_item.*
import java.util.*


class ItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        setSupportActionBar(item_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        @Suppress("DEPRECATION")
        val date = Date(intent.getStringExtra("publishedAt"))

        val newsItem = NewsItem(intent.getStringExtra("author"), intent.getStringExtra("title"),
                intent.getStringExtra("description"), intent.getStringExtra("url"),
                intent.getStringExtra("urlToImage"), date)

        if (newsItem.urlToImage != null)
            Glide.with(this@ItemActivity)
                    .load(Uri.parse(newsItem.urlToImage))
                    .into(item_img)

        supportActionBar!!.title = newsItem.title
        if (newsItem.author != null)
            supportActionBar!!.subtitle = newsItem.author

        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this@ItemActivity)

        recyclerViewItem.adapter = ItemAdapter(newsItem, this@ItemActivity)

    }

    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}