package com.wkwn.newwws

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener({
            if(hasConnection())
                AsyncHandler.RequestTask(recyclerView, this).execute(UrlApi().create(UrlApi.Category.DEFAULT, "ru"))
            else
                Toast.makeText(this, "Нет соединения", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
        })

        // TODO: Взятие данных из БД, если она пуста, то onRefresh()
        if (hasConnection())
            AsyncHandler.RequestTask(recyclerView, this).execute(UrlApi().create(UrlApi.Category.DEFAULT, "ru"))
        else
            Toast.makeText(this, "Нет соединения", Toast.LENGTH_LONG).show()
    }

    private fun hasConnection(): Boolean {
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        var netInfo: NetworkInfo? = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (netInfo != null && netInfo.isConnected)
            return true

        netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (netInfo != null && netInfo.isConnected)
            return true

        netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
