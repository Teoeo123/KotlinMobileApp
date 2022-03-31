package com.example.profeska

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.handyopnion.LoadingScreen
import com.handyopnion.LoadingScreen.hideLoading
import pl.droidsonroids.gif.GifDrawable
import java.io.File

class MyAdapter(private val dataArray: ArrayList<DatabaseEvent>): RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventRow = layoutInflater.inflate(R.layout.event_row_layout, parent, false)

       /// LoadingScreen.displayLoadingWithText(parent.context,"Please wait...",false)

        return MyViewHolder(eventRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photo = dataArray[holder.adapterPosition].photo
        holder.name.text=dataArray[holder.adapterPosition].name
        holder.des.text=dataArray[holder.adapterPosition].description


        val call = itemCount

        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("events/$photo")
        val localFile = File.createTempFile("temp","png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.pic.setImageBitmap(bitmap)

              holder.bar.alpha = 0.toFloat()

            //holder.gif.setVisibility(View.GONE)

         //   if(holder.adapterPosition==call-1){
          //      hideLoading()
         //   }




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

    val bar = view.findViewById(R.id.pBar) as ProgressBar

}