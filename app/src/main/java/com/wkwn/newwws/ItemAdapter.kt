package com.wkwn.newwws

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle


class ItemAdapter (private val author: String?, private val title: String?, private val description: String?,
                   private val url: String?, private val urlToImage: String?, private val publishedAt: String?,
                   private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = title
        holder.description.text = description
        holder.author.text = author
        holder.publishedAt.text = publishedAt

        if (urlToImage != null)
            Picasso.with(context).load(Uri.parse(urlToImage)).resize(holder.description.width, 600).into(holder.img)

        holder.btn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
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