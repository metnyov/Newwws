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
import com.bumptech.glide.request.RequestOptions
import com.wkwn.newwws.models.News
import com.wkwn.newwws.R
import com.wkwn.newwws.activities.ItemActivity


class ListAdapter(val data: News, private val context: Context) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tmpTitle = data.articles[position].title

        if (data.articles[position].urlToImage == null)
            Glide.with(context).load(R.drawable.newspaper).into(holder.img)
        else
            Glide.with(context).load(Uri.parse(data.articles[position].urlToImage))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.newspaper))
                    .into(holder.img)


        holder.title.text = if (tmpTitle.length <= 70) tmpTitle else tmpTitle.substring(0..70) + "..."
        holder.date.text = data.articles[position].getFormattedDateString("d MMMM")
        holder.time.text = data.articles[position].getFormattedDateString("HH:mm")

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
        val time: TextView = itemView.findViewById(R.id.main_time)
        val date: TextView = itemView.findViewById(R.id.main_date)
        val img: ImageView = itemView.findViewById(R.id.main_img)
    }
}