package com.example.profeska

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MyAdapter(private val dataArray: ArrayList<DatabaseEvent>): RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventRow = layoutInflater.inflate(R.layout.event_row_layout, parent, false)
        return MyViewHolder(eventRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photo = dataArray[holder.adapterPosition].photo
        holder.name.text=dataArray[holder.adapterPosition].name
        holder.des.text=dataArray[holder.adapterPosition].description
        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("events/$photo")
        val localFile = File.createTempFile("temp","png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.pic.setImageBitmap(bitmap)
        }

    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

}

class MyViewHolder(private val view: View): RecyclerView.ViewHolder(view)
{
    val name=view.findViewById(R.id.eName) as TextView
    val des=view.findViewById(R.id.eDes) as TextView
    val pic=view.findViewById(R.id.eventImg) as ImageView
}