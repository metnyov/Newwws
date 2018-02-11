package com.wkwn.newwws

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class ListAdapter(val data: News, private val context: Context) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmpTitle = data.articles[position].title
        val tmpUrlToImage = data.articles[position].urlToImage
        val tmpPublishedAt = data.articles[position].publishedAt

        holder.title.text = if (tmpTitle.length < 90) tmpTitle else tmpTitle.substring(0..90) + "..."

        if (tmpUrlToImage != null)
            Picasso.with(context).load(tmpUrlToImage).into(holder.img)
        else
            Picasso.with(context).load("http://www.mydog.am/images/noimage.png").into(holder.img)

        holder.publishedAt.text = tmpPublishedAt.toLocaleString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.articles.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.main_title)
        val publishedAt: TextView = itemView.findViewById(R.id.main_date)
        val img: ImageView = itemView.findViewById(R.id.main_img)
    }
}