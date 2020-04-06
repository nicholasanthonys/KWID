package com.ppb.kwid.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "KWID.db"
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

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
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


    fun deleteFavoriteMovies(user_id: String, movie_id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, COL_USER_ID +"= ? AND " + COL_MOVIE_ID + "= ?", arrayOf(user_id, movie_id))
    }

    fun isCurrentMovieLiked (user_id : String, movie_id: String) : Boolean {
        val db = this.readableDatabase
        val countQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_USER_ID = '" + user_id +"'" + " AND $COL_MOVIE_ID = " + movie_id
        val cursor :Cursor = db.rawQuery(countQuery,null)
        val count = cursor.count
        cursor.close()
        if(count > 0) {
            return true
        }
        return false
    }
}