package com.wkwn.newwws

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_item.*


class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val author = intent.getStringExtra("author")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val url = intent.getStringExtra("url")
        val urlToImage = intent.getStringExtra("urlToImage")
        val publishedAt = intent.getStringExtra("publishedAt")

        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        recyclerViewItem.adapter = ItemAdapter(author, title, description,
                url, urlToImage, publishedAt, this)
    }
}