package com.ppb.kwid.Activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ppb.kwid.Model.Location.LocationAdapter
import com.ppb.kwid.R

class LocationActivity : AppCompatActivity() {
    private lateinit var btnBack: Button
    private lateinit var rv_location: RecyclerView
    // Access a Cloud Firestore instance from your Activity
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        btnBack = findViewById(R.id.btn_back_location)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        rv_location = findViewById(R.id.rv_location)
        showRecyclerListLocation()
    }

    private fun showRecyclerListLocation() {
        var listLocationFromDatabase: MutableList<String> = mutableListOf()
        rv_location.layoutManager = LinearLayoutManager(this)
        //get list
        db.collection("currentlyShowingCity")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    listLocationFromDatabase.add(document.data.get("city").toString())
                }

                val listLocationAdapter = LocationAdapter(listLocationFromDatabase)

                //end of get list
                rv_location.adapter = listLocationAdapter

            }


    }
}
