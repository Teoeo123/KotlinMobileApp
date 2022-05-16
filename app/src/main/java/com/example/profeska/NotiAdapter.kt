package com.example.profeska

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Half.toFloat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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

class NotiAdapter(private val dataArray: ArrayList<WaitingClass>): RecyclerView.Adapter<NotiViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventRow = layoutInflater.inflate(R.layout.noti_row_layout, parent, false)
        return NotiViewHolder(eventRow)
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        val firebase=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        firebase.getReference("users").get().addOnSuccessListener {
            val fName = it.child(dataArray[holder.adapterPosition].user.toString()).child("name").value.toString()
            val sName=it.child(dataArray[holder.adapterPosition].user.toString()).child("sname").value.toString()
            holder.name.text="$fName $sName"
            firebase.getReference("events").child("all").get().addOnSuccessListener {
                val pName = it.child(dataArray[holder.adapterPosition].event.toString()).child("name").value.toString()
                holder.partyName.text=pName.toString()
            }.addOnFailureListener {Log.d("DATABASE_CONNECTION","FAILED")}
            holder.linLayout.alpha= 1.toFloat()
            holder.name
        }.addOnFailureListener { Log.d("DATABASE_CONNECTION","FAILED") }

        val ref:DatabaseReference =FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app").getReference("events").child("all")
        val acceptUid=dataArray[holder.adapterPosition].user
        val eventUid=dataArray[holder.adapterPosition].event

        holder.accept.setOnClickListener {
            ref.child("$eventUid").child("waiting").child("$acceptUid").removeValue()
            ref.child("$eventUid").child("accepted").child("$acceptUid").setValue(acceptUid)
            firebase.getReference("users").child("$acceptUid").child("accepted").push().setValue("$eventUid")
            holder.accept.isClickable = false
            holder.discard.isClickable = false
            holder.linLayout.alpha=0.toFloat()
            holder.info.text="zaakceptowany"
            holder.info.setTextColor(Color.parseColor("#02DE61"))
            holder.info.alpha=1.toFloat()

        }

        holder.discard.setOnClickListener {
            ref.child("$eventUid").child("waiting").child("$acceptUid").removeValue()
            ref.child("$eventUid").child("rejected").child("$acceptUid").setValue(acceptUid)
            holder.accept.isClickable = false
            holder.discard.isClickable = false
            holder.linLayout.alpha=0.toFloat()
            holder.info.text="odrzucony"
            holder.info.setTextColor(Color.parseColor("#DE0B2C"))
            holder.info.alpha=1.toFloat()
        }

    }

}

class NotiViewHolder(private val view: View): RecyclerView.ViewHolder(view)
{
    val name=view.findViewById(R.id.notiNick) as TextView
    val partyName=view.findViewById(R.id.notiPartyName) as TextView
    val pic=view.findViewById(R.id.notiImg) as ImageView
    val linLayout= view.findViewById(R.id.notiLinLayout) as LinearLayout
    val accept=view.findViewById(R.id.notiAcept) as Button
    val discard=view.findViewById(R.id.notiDiscard) as Button
    val info=view.findViewById(R.id.tv_ac_info) as TextView
}