package com.wkwn.newwws.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.os.Bundle
import com.wkwn.newwws.models.NewsItem
import com.wkwn.newwws.R
import com.wkwn.newwws.activities.BrowserActivity


class ItemAdapter (private val newsItem: NewsItem,
                   private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = newsItem.title
        holder.description.text = newsItem.description
        holder.publishedAt.text = newsItem.getFormattedDateString("d MMMM yyyy  HH:mm:ss")

        holder.btn.setOnClickListener {
            val intent = Intent(context, BrowserActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("url", newsItem.url)
            startActivity(context, intent, Bundle())
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
        val publishedAt: TextView = itemView.findViewById(R.id.item_date)
        val btn: Button = itemView.findViewById(R.id.readBtn)
    }
}