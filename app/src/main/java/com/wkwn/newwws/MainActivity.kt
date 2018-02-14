package com.wkwn.newwws

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.net.ConnectivityManager
import android.widget.Toast


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (hasConnection())
            AsyncHandler.RequestTask(recyclerView, this).execute(UrlApi().create(UrlApi.Category.DEFAULT, "ru"))
        else
        //TODO: Взятие данных из БД
            Toast.makeText(this, "Нет соединения", Toast.LENGTH_LONG).show()

        swipeRefreshLayout.setOnRefreshListener({
            if(hasConnection())
                AsyncHandler.RequestTask(recyclerView, this).execute(UrlApi().create(UrlApi.Category.DEFAULT, "ru"))
            else
                Toast.makeText(this, "Нет соединения", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
        })
    }

    private fun hasConnection(): Boolean {
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}