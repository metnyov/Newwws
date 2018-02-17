package com.wkwn.newwws.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.wkwn.newwws.News
import com.wkwn.newwws.R
import com.wkwn.newwws.activities.ItemActivity


class ListAdapter(val data: News, private val context: Context) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noPhotoUrl: Uri = Uri.parse("http://image.ibb.co/nx9ZO7/gazeta.png")

        val tmpTitle = data.articles[position].title

        val tmpUrlToImage: Uri? = if (data.articles[position].urlToImage == null) noPhotoUrl
            else {
                val tmp = Uri.parse(data.articles[position].urlToImage)
                if (tmp.scheme != null) tmp
                else noPhotoUrl
            }
        Glide.with(context).load(tmpUrlToImage).into(holder.img)

        holder.title.text = if (tmpTitle.length <= 100) tmpTitle else tmpTitle.substring(0..100) + "..."

        holder.publishedAt.text = data.articles[position].getFormattedDateString("dd.MM.yy HH:mm")

        holder.itemView.setOnClickListener({
            val intent = Intent(context, ItemActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("author", data.articles[position].author)
            intent.putExtra("title", data.articles[position].title)
            intent.putExtra("description", data.articles[position].description)
            intent.putExtra("url", data.articles[position].url)
            intent.putExtra("urlToImage", data.articles[position].urlToImage)
            intent.putExtra("publishedAt", data.articles[position].publishedAt.toString())
            startActivity(context, intent, Bundle())
        })
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