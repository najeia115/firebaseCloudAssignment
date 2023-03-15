package com.example.firebasecloudassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Home : AppCompatActivity() {

    private lateinit var rvNotes: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var btnAdd : ImageView
    private var noteArrayList = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvNotes = findViewById(R.id.rvNotes)
        btnAdd = findViewById(R.id.iconAdd)
        noteAdapter = NoteAdapter(ArrayList())

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = noteAdapter
        noteAdapter.notifyDataSetChanged()

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddNote::class.java))
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("notes")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                        val note = document.toObject(Note::class.java)
                        note.id = document.id
                        noteArrayList.add(note)

                }
                noteAdapter = NoteAdapter( noteArrayList)
                val recyclerView = findViewById<RecyclerView>(R.id.rvNotes)
                recyclerView.adapter = noteAdapter
            }

            .addOnFailureListener {
                Log.e("najeia", "loadCategories: ")
            }



    }
}