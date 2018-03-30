package com.wkwn.newwws.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*


class DBHelper(country: String, context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME + country, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "db-nws-"

        // tables:
        const val TABLE_DEFAULT = "head"
        const val TABLE_BUSINESS = "business"
        const val TABLE_HEALTH = "health"
        const val TABLE_SPORTS = "sports"
        const val TABLE_TECHNOLOGY = "technology"
        const val TABLE_ENTERTAINMENT = "entertainment"

        // columns news:
        const val KEY_AUTHOR = "author"
        const val KEY_TITLE = "title"
        const val KEY_DESCRIPTION = "description"
        const val KEY_URL = "url"
        const val KEY_IMAGE_URL = "pathToImage"
        const val KEY_DATE = "publishedAt"

    }

    private val tables = arrayListOf(TABLE_DEFAULT, TABLE_BUSINESS, TABLE_HEALTH, TABLE_SPORTS, TABLE_TECHNOLOGY, TABLE_ENTERTAINMENT)

    override fun onCreate(db: SQLiteDatabase?) {
        for (table in tables)
            db?.execSQL("create table $table(" +
                    "$KEY_AUTHOR text, " +
                    "$KEY_TITLE text, " +
                    "$KEY_DESCRIPTION text, " +
                    "$KEY_URL text primary key, " +
                    "$KEY_IMAGE_URL text, " +
                    "$KEY_DATE integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        for (table in tables)
            db?.execSQL("drop table if exist $table")

        onCreate(db)
    }

    fun toNews(table: String = TABLE_DEFAULT, limit: Int = 0): News{

        val news = News(arrayListOf())
        val db = this@DBHelper.readableDatabase
        val cursor = if (limit > 0)
            db.query(table, null, null, null,
                    null, null, "$KEY_DATE DESC", limit.toString())
        else
            db.query(table, null, null, null,
                    null, null, "$KEY_DATE DESC")
        if (cursor.moveToFirst()) {
            do {
                @Suppress("DEPRECATION")
                val item = NewsItem(
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_IMAGE_URL)),
                        Date(cursor.getLong(cursor.getColumnIndex(KEY_DATE)))
                )
                news.articles.add(item)
            } while (cursor.moveToNext())
        }
        else {
            cursor.close()
            return News(arrayListOf())
        }
        cursor.close()
        return news
    }

    fun checkCompare(table: String = TABLE_DEFAULT, url: String): Boolean{
        val db = this@DBHelper.readableDatabase
        val cursor = db.query(table, null, null, null, null, null, "$KEY_DATE DESC")

        val res = if (cursor.moveToFirst())
            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL)) == url
        else
            false

        cursor.close()

        return res
    }

    fun isEmpty (table: String = TABLE_DEFAULT): Boolean {
        val db = this@DBHelper.readableDatabase
        val cursor = db.rawQuery("select * from $table", null)
        val res = !cursor.moveToFirst()
        cursor.close()
        return res
    }

}