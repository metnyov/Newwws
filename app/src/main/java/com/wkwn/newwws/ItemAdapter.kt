package com.wkwn.newwws

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide


class ItemAdapter (private val newsItem: NewsItem,
                   private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = newsItem.title
        holder.description.text = newsItem.description
        holder.author.text = newsItem.author
        holder.publishedAt.text = newsItem.getFormattedDateString("d MMMM yyyy  HH:mm:ss")

        if (newsItem.urlToImage != null)
            Glide.with(context).load(Uri.parse(newsItem.urlToImage)).into(holder.img)

        holder.btn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.url))
            startActivity(context, browserIntent, Bundle())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = 1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.item_title)
        val description: TextView = itemView.findViewById(R.id.item_description)
        val author: TextView = itemView.findViewById(R.id.item_author)
        val publishedAt: TextView = itemView.findViewById(R.id.item_date)
        val img: ImageView = itemView.findViewById(R.id.item_img)
        val btn: Button = itemView.findViewById(R.id.readBtn)
    }
}