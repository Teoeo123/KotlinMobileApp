package com.example.profeska

import android.widget.*

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.*
import java.io.File

class ChatPickAdapter(private val dataArray: ArrayList<String>): RecyclerView.Adapter<ChatPickerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatPickerViewHolder {
        Log.d("ADAPTER","responding")
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventRow = layoutInflater.inflate(R.layout.event_row_layout, parent, false)
        return ChatPickerViewHolder(eventRow)
    }

    override fun onBindViewHolder(holder: ChatPickerViewHolder, position: Int) {
        Log.d("ViewHolder","responding")
        val id=dataArray[position]

        val ref=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("events").child("all").child("$id")

        ref.get().addOnSuccessListener {

            val photo = it.child("photo").value.toString()
            holder.name.text= it.child("name").value.toString()
            holder.des.text=it.child("description").value.toString()

            if(it.child("date").value.toString()!="null"){
                holder.time.text= displayFormatTime(it.child("date").value.toString())
                holder.date.text= displayFormatDate(it.child("date").value.toString())
            }else{
                holder.time.alpha=0.toFloat()
                holder.date.alpha=0.toFloat()
            }

            holder.click.setOnClickListener{
                Log.d("test","klikanie działa")
                val myIntent= Intent(holder.itemView.context, ChatActivity::class.java)
                myIntent.putExtra("id",photo).putExtra("name","${holder.name.text}")
                startActivity(holder.itemView.context,myIntent,null)

            }

            val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("events/$photo")
            val localFile = File.createTempFile("temp","png")

            storageRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                holder.pic.setImageBitmap(bitmap)
                holder.pbar.alpha=0.toFloat()

            }


        }
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

}

class ChatPickerViewHolder(private val view: View): RecyclerView.ViewHolder(view)
{
    val name=view.findViewById(R.id.eName) as TextView
    val des=view.findViewById(R.id.eDes) as TextView
    val pic=view.findViewById(R.id.eventImg) as ImageView
    val click=view.findViewById(R.id.relativeLayout) as ConstraintLayout
    val time=view.findViewById(R.id.tvShowTime) as TextView
    val date=view.findViewById(R.id.tvShowDate) as TextView
    val pbar=view.findViewById(R.id.pBar) as ProgressBar
}