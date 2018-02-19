package com.wkwn.newwws.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*


class DBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "newsDB"

        // tables:
        const val TABLE_DEFAULT = "head"
        const val TABLE_BUSINESS = "business"
        const val TABLE_HEALTH = "health"
        const val TABLE_SPORTS = "sports"
        const val TABLE_TECHNOLOGY = "technology"
        const val TABLE_ENTERTAINMENT = "entertainment"

        // columns:
        const val KEY_ID = "_id"
        const val KEY_AUTHOR = "author"
        const val KEY_TITLE = "title"
        const val KEY_DESCRIPTION = "description"
        const val KEY_URL = "url"
        const val KEY_PATH_TO_IMAGE = "pathToImage"
        const val KEY_PUBLISHED_AT = "publishedAt"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table " + TABLE_DEFAULT + "(" + KEY_ID
                + " integer primary key," + KEY_AUTHOR + " text," + KEY_TITLE + " text,"
                + KEY_DESCRIPTION + " text," + KEY_URL + " text,"
                + KEY_PATH_TO_IMAGE + " text," + KEY_PUBLISHED_AT + " text" + ");")

        db?.execSQL("create table " + TABLE_BUSINESS + "(" + KEY_ID
                + " integer primary key," + KEY_AUTHOR + " text," + KEY_TITLE + " text,"
                + KEY_DESCRIPTION + " text," + KEY_URL + " text,"
                + KEY_PATH_TO_IMAGE + " text," + KEY_PUBLISHED_AT + " text" + ");")

        db?.execSQL("create table " + TABLE_HEALTH + "(" + KEY_ID
                + " integer primary key," + KEY_AUTHOR + " text," + KEY_TITLE + " text,"
                + KEY_DESCRIPTION + " text," + KEY_URL + " text,"
                + KEY_PATH_TO_IMAGE + " text," + KEY_PUBLISHED_AT + " text" + ");")

        db?.execSQL("create table " + TABLE_SPORTS + "(" + KEY_ID
                + " integer primary key," + KEY_AUTHOR + " text," + KEY_TITLE + " text,"
                + KEY_DESCRIPTION + " text," + KEY_URL + " text,"
                + KEY_PATH_TO_IMAGE + " text," + KEY_PUBLISHED_AT + " text" + ");")

        db?.execSQL("create table " + TABLE_TECHNOLOGY + "(" + KEY_ID
                + " integer primary key," + KEY_AUTHOR + " text," + KEY_TITLE + " text,"
                + KEY_DESCRIPTION + " text," + KEY_URL + " text,"
                + KEY_PATH_TO_IMAGE + " text," + KEY_PUBLISHED_AT + " text" + ")")

        db?.execSQL("create table " + TABLE_ENTERTAINMENT + "(" + KEY_ID
                + " integer primary key," + KEY_AUTHOR + " text," + KEY_TITLE + " text,"
                + KEY_DESCRIPTION + " text," + KEY_URL + " text,"
                + KEY_PATH_TO_IMAGE + " text," + KEY_PUBLISHED_AT + " text" + ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exist " + TABLE_DEFAULT)
        db?.execSQL("drop table if exist " + TABLE_BUSINESS)
        db?.execSQL("drop table if exist " + TABLE_HEALTH)
        db?.execSQL("drop table if exist " + TABLE_SPORTS)
        db?.execSQL("drop table if exist " + TABLE_TECHNOLOGY)
        db?.execSQL("drop table if exist " + TABLE_ENTERTAINMENT)
        onCreate(db)
    }

    fun toNews(table: String = TABLE_DEFAULT): News{

        val news = News(arrayListOf())
        val db = this.readableDatabase

        val cursor = db.query(table, null, null, null, null, null, KEY_ID + " DESC")
        if (cursor.moveToFirst()) {
            do {
                @Suppress("DEPRECATION")
                val item = NewsItem(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PATH_TO_IMAGE)),
                        Date(cursor.getString(cursor.getColumnIndex(KEY_PUBLISHED_AT)))
                        )
                news.articles.add(item)
            } while (cursor.moveToNext())
        } else
            Log.d("mLog", table + " IS EMPTY!")

        return news
    }

    fun checkCompareId (table: String = TABLE_DEFAULT, id: Long, url: String?): Boolean{
        val db = this.readableDatabase
        val cursor = db.query(table, null, null, null, null, null, KEY_ID + " DESC")
        return if (cursor.moveToFirst())
            cursor.getLong(cursor.getColumnIndex(DBHelper.KEY_ID)) == id &&
                    cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL)) == url
        else
            false
    }

    fun isEmptyTable (table: String = TABLE_DEFAULT): Boolean{
        val db = this.readableDatabase
        val cursor = db.query(table, null, null, null, null, null, null)
        return cursor.moveToFirst()
    }

}