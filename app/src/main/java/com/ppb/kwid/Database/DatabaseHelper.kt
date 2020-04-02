package com.ppb.kwid.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.ppb.kwid.Database.DatabaseHelper.FeedEntry.COL_DIRECTOR
import com.ppb.kwid.Database.DatabaseHelper.FeedEntry.COL_GENRE
import com.ppb.kwid.Database.DatabaseHelper.FeedEntry.COL_MOVIE_ID
import com.ppb.kwid.Database.DatabaseHelper.FeedEntry.COL_TILTE
import com.ppb.kwid.Database.DatabaseHelper.FeedEntry.COL_USER_ID
import com.ppb.kwid.Database.DatabaseHelper.FeedEntry.TABLE_NAME



class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val dbHelper = DatabaseHelper(context)

    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "table_favorite_movies"
        const val COL_USER_ID = "user_id"
        const val COL_MOVIE_ID = "movie_id"
        const val COL_TILTE = "movie_title"
        const val COL_GENRE = "genre"
        const val COL_DIRECTOR = "director"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME (user_id TEXT,  " +
                    "movie_id TEXT, movie_title TEXT, genre TEXT, director TEXT )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertTableFavoriteMovies(
        user_id: String,
        movie_id: String,
        title: String,
        genre: String,
        director: String
    ) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_USER_ID, user_id)
        contentValues.put(COL_MOVIE_ID, movie_id)
        contentValues.put(COL_TILTE, title)
        contentValues.put(COL_GENRE, genre)
        contentValues.put(COL_DIRECTOR, director)
        db.insert(TABLE_NAME, null, contentValues)
    }

    /**
     * Let's create a function to delete a given row based on the id.
     */
    fun deleteFavoriteMovies(user_id: String, movie_id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "user_id = ? AND movie_id = ?", arrayOf(user_id, movie_id))
    }

    /**
     * The below getter property will return a Cursor containing our dataset.
     */
    val allData: Cursor
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            return res
        }


    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "KWID.db"
    }


}