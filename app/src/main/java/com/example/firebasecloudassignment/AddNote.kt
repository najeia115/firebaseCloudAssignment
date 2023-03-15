package com.example.firebasecloudassignment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.InternalChannelz.id
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnCreate: Button
    private lateinit var firestore: FirebaseFirestore

    companion object{
        val TAG = "addNote"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_add)

        val newNote = Note()
        firestore = FirebaseFirestore.getInstance()

        etTitle = findViewById(R.id.et_title)
        etContent = findViewById(R.id.et_content)
        btnCreate = findViewById(R.id.btn_create)
        btnCreate.setOnClickListener {

            val note = hashMapOf(
                "title" to etTitle.text.toString(),
                "content" to etContent.text.toString(),
            )
            firestore.collection("notes")
                .add(note)
                .addOnSuccessListener { documentReference ->
                    val noteId = documentReference.id
                    newNote.id = noteId
                    Log.d(TAG, "Note added with ID: ${newNote.id}")
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding note", e)
                }

        }


    }
}