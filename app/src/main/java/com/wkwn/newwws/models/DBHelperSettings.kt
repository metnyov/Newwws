package com.wkwn.newwws.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelperSettings(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "settings"

        // tables
        const val TABLE_SETTINGS = "settings"

        // columns
        const val KEY_COUNTRY = "country"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_SETTINGS($KEY_COUNTRY text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exist $TABLE_SETTINGS")
        onCreate(db)
    }

    fun isEmpty(): Boolean {
        val cursor = this.readableDatabase.rawQuery("select * from $TABLE_SETTINGS", null)
        val res = !cursor.moveToFirst()
        cursor.close()
        return res
    }

    fun setCountry (country: String) = this.writableDatabase.execSQL("update $TABLE_SETTINGS set $KEY_COUNTRY = '$country'")

    fun getCountry (): String {
        val cursor = this.readableDatabase.rawQuery("select $KEY_COUNTRY from $TABLE_SETTINGS", null)
        val res = if (cursor.moveToFirst())
            cursor.getString(cursor.getColumnIndex(KEY_COUNTRY))
        else
            ""
        cursor.close()
        return res
    }

}