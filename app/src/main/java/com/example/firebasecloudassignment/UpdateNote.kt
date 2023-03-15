package com.example.firebasecloudassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateNote : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var noteRef: DocumentReference
    private lateinit var editTitleText: EditText
    private lateinit var editContentText: EditText
    private lateinit var save : Button
    private lateinit var note: Note


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_update)

        db = FirebaseFirestore.getInstance()
        editTitleText = findViewById(R.id.editTitle)
        editContentText = findViewById(R.id.editContent)
        save = findViewById(R.id.btnSave)

        noteRef = db.collection("notes").document(intent.getStringExtra("noteId")!!)

        noteRef.get().addOnSuccessListener { documentSnapshot ->
            note = documentSnapshot.toObject(Note::class.java)!!
            editTitleText.setText(note.title)
            editContentText.setText(note.content)


            save.setOnClickListener {
                val newTitle = editTitleText.text.toString().trim()
                val newContent = editContentText.text.toString().trim()

                note.title = newTitle
                note.content = newContent

                noteRef.set(note).addOnSuccessListener {
                    Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Home::class.java))
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
                }
            }

        }.addOnFailureListener { exception ->
            Log.d("updateNote", "Failed to get note data: $exception")
            Toast.makeText(this, "Failed to get note data", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}

