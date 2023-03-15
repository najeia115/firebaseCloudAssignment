package com.example.firebasecloudassignment

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class NoteAdapter( var data : ArrayList<Note>) : RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    private lateinit var db : FirebaseFirestore

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        var contentTextView: TextView = itemView.findViewById(R.id.tvContent)
        var deleteIcon :ImageView = itemView.findViewById(R.id.deleteIcon)
        var updateIcon :ImageView = itemView.findViewById(R.id.updateIcon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = data[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.updateIcon.setOnClickListener {

                val intent = Intent(holder.titleTextView.context, UpdateNote::class.java)
                intent.putExtra("noteId",note.id)
                Log.d("adapterId", "noteId: ${note.id}")
                holder.itemView.context.startActivity(intent)
        }


        holder.deleteIcon.setOnClickListener {
            val builder = AlertDialog.Builder(holder.titleTextView.getContext())
            builder.setTitle("Are you sure?")
            builder.setMessage("You want to delete this item?")

            builder.setPositiveButton("Delete") { d, i ->
                db = FirebaseFirestore.getInstance()
                val noteRef = db.collection("notes").document(note.id)
                noteRef.get().addOnSuccessListener { documentSnapshot ->
                        noteRef.delete()
                        Toast.makeText(holder.titleTextView.getContext(),"Deleted Successfully.", Toast.LENGTH_SHORT).show()
                        data.removeAt(position)
                        notifyDataSetChanged()
                }
            }
            builder.setNegativeButton("Cancel") { d, i ->
                Toast.makeText(holder.titleTextView.getContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            }

            builder.create()
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}