package com.ppb.kwid.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "KWID.db"
        const val TABLE_FAVORITE = "table_favorite_movies"
        const val COL_USER_ID = "user_id"
        const val COL_MOVIE_ID = "movie_id"
        const val COL_TILTE = "movie_title"
        const val COL_GENRE = "genre"
        const val COL_DIRECTOR = "director"

        const val TABLE_USERS = "table_users"
        const val COL_USERNAME = "username"
        const val COL_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_FAVORITE (" +
                    "user_id TEXT,  " +
                    "movie_id TEXT, " +
                    "movie_title TEXT, " +
                    "genre TEXT, " +
                    "director TEXT," +
                    "PRIMARY KEY (user_id,movie_id)" +
                    ")"
        )

        db.execSQL(
            "CREATE TABLE $TABLE_USERS (" +
                    "username TEXT," +
                    " email TEXT, " +
                    "PRIMARY KEY (username, email)" +
                    ")"
        )
    }

    fun insertUser(username: String, email: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_USERNAME, username)
        contentValues.put(COL_EMAIL, email)
        try {
            db.insert(TABLE_USERS, null, contentValues)
            println("insert username email sukses")
        } catch (e: SQLiteException) {
            println(e.printStackTrace())
        }


    }

    fun updateUsername(username: String, email: String) {
        if (username.isNotEmpty()) {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(COL_USERNAME, username)
            db.update(TABLE_USERS, contentValues, "email = ?", arrayOf(email))
            print("updated")
        }
        print("username is empty")
    }

    fun getUsername(email: String?): String {
        Log.d("email is ", email)
        val db = this.readableDatabase

        var cursor: Cursor = db.rawQuery(
            "Select $COL_USERNAME from $TABLE_USERS where $COL_EMAIL ='" + email + "'",
            null
        )

        var username = ""
        if (cursor.moveToFirst()) {
            username = (cursor.getString(cursor.getColumnIndex(COL_USERNAME)))
            while (cursor.moveToNext()) {
                username = (cursor.getString(cursor.getColumnIndex(COL_USERNAME)))
            }
        }
        cursor.close()
        db.close()
        print("username is " + username)
        return username

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
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
        db.insert(TABLE_FAVORITE, null, contentValues)
    }


    fun deleteFavoriteMovies(user_id: String, movie_id: String) {
        val db = this.writableDatabase
        db.delete(
            TABLE_FAVORITE,
            COL_USER_ID + "= ? AND " + COL_MOVIE_ID + "= ?",
            arrayOf(user_id, movie_id)
        )
    }

    fun isCurrentMovieLiked (user_id : String, movie_id: String) : Boolean {
        val db = this.readableDatabase
        val countQuery =
            "SELECT * FROM $TABLE_FAVORITE WHERE $COL_USER_ID = '" + user_id + "'" + " AND $COL_MOVIE_ID = " + movie_id
        val cursor :Cursor = db.rawQuery(countQuery,null)
        val count = cursor.count
        cursor.close()
        if(count > 0) {
            return true
        }
        return false
    }

    fun getAllFavoriteMovies(user_id: String): MutableList<String> {
        val db = this.readableDatabase
        var listIDFavoriteMovies = mutableListOf<String>()
        var cursor: Cursor = db.rawQuery(
            "Select $COL_MOVIE_ID from $TABLE_FAVORITE where $COL_USER_ID ='" + user_id + "'",
            null
        )

        if (cursor.moveToFirst()) {
            listIDFavoriteMovies.add(cursor.getString(cursor.getColumnIndex(COL_MOVIE_ID)))
            while (cursor.moveToNext()) {
                listIDFavoriteMovies.add(cursor.getString(cursor.getColumnIndex(COL_MOVIE_ID)))
            }
        }
        cursor.close()
        db.close()
//        println("print id di dalam list")
//        for(item in listIDFavoriteMovies){
//            println(item)
//        }

        return listIDFavoriteMovies
    }
}