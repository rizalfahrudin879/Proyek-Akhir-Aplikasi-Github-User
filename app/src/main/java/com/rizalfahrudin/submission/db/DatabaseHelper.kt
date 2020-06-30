package com.rizalfahrudin.submission.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rizalfahrudin.submission.db.DatabaseContract.UserColumn.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbuser"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.UserColumn._ID} INTEGER PRIMARY KEY," +
                " ${DatabaseContract.UserColumn.USERNAME} TEXT NO NULL," +
                " ${DatabaseContract.UserColumn.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumn.AVA} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumn.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumn.LOCATION} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumn.REPOS} INTEGER NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}