package com.example.profeska

import android.annotation.SuppressLint
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UserAdapter(private val dataArray: ArrayList<String>): RecyclerView.Adapter<UserViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventRow = layoutInflater.inflate(R.layout.user_row_layout, parent, false)
        return UserViewHolder(eventRow)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Log.d("ID",dataArray[holder.adapterPosition])
        val ref=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(dataArray[holder.adapterPosition])
            .get().addOnSuccessListener {
                var n= it.child("name").value.toString()
                var sn= it.child("sname").value.toString()
                Log.d("name", n)
                Log.d("sname", sn)
                holder.name.text="$n $sn"
            }
        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users").child(dataArray[holder.adapterPosition])
        val localFile = File.createTempFile("temp","png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.pic.setImageBitmap(bitmap)
            holder.pbar.alpha=0.toFloat()

        }

    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

}

class UserViewHolder(private val view: View): RecyclerView.ViewHolder(view)
{
    val pbar = view.findViewById(R.id.pBar) as ProgressBar
    val name=view.findViewById(R.id.tvNameU) as TextView
    val pic=view.findViewById(R.id.userImg) as ImageView
    val click=view.findViewById(R.id.userRowLayout) as ConstraintLayout
}