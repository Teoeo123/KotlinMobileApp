package com.example.profeska

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
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
        holder.city.text=dataArray[holder.adapterPosition].city
        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("events/$photo")
        val localFile = File.createTempFile("temp","png")
        if(dataArray[holder.adapterPosition].date.toString()!="null"){
            holder.time.text= displayFormatTime(dataArray[holder.adapterPosition].date.toString())
        }else{
            holder.time.alpha=0.toFloat()
        }
        if(dataArray[holder.adapterPosition].date.toString()!="null"){
            holder.date.text= displayFormatDate(dataArray[holder.adapterPosition].date.toString())
        }else{
            holder.date.alpha=0.toFloat()
        }


        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.pic.setImageBitmap(bitmap)
            holder.pbar.alpha=0.toFloat()

        }
        holder.click.setOnClickListener{
            Log.d("test","klikanie działa")
            val myIntent= Intent(holder.itemView.context,EventShowActivity::class.java)
            myIntent.putExtra("id",photo)
            startActivity(holder.itemView.context,myIntent,null)

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
    val click=view.findViewById(R.id.relativeLayout) as ConstraintLayout
    val time=view.findViewById(R.id.tvShowTime) as TextView
    val date=view.findViewById(R.id.tvShowDate) as TextView
    val pbar=view.findViewById(R.id.pBar) as ProgressBar
    val city=view.findViewById(R.id.tvCity) as TextView
}
