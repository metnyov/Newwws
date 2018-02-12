package com.wkwn.newwws

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class ItemAdapter (private val author: String?, private val title: String?, private val description: String?,
                   private val url: String?, private val urlToImage: String?, private val publishedAt: String?,
                   private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = title
        holder.description.text = description
        holder.author.text = author
        holder.publishedAt.text = publishedAt

        if (urlToImage != null)
            Picasso.with(context).load(urlToImage).into(holder.img)
        else
            Picasso.with(context).load("http://resrahod.org.in/media/no-image.png").into(holder.img)
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
    }
}