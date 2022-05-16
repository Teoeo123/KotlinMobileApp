package com.example.profeska

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.Gravity
import androidx.cardview.widget.CardView
import android.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class ChatAdapter(private val dataArray: ArrayList<MessageClass>): RecyclerView.Adapter<ChatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        Log.d("ADAPTER","responding")
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventRow = layoutInflater.inflate(R.layout.message_row_layout, parent, false)
        return ChatViewHolder(eventRow)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val usId=FirebaseAuth.getInstance().uid
        if(dataArray[position].id ==usId )
        {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1.0f
                gravity = Gravity.RIGHT
                marginEnd=2
            }
            holder.box.layoutParams = params;
        }
        else
        {
            holder.box.setCardBackgroundColor(Color.argb(255, 242, 173, 12))

            val photo=dataArray[position].id
            val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$photo")
            val localFile = File.createTempFile("temp","png")

            storageRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                holder.pic.setImageBitmap(bitmap)
            }.addOnCanceledListener {
                    Log.d("PHOTO","CANT FIND")
                }
        }
        holder.mess.text=dataArray[position].mess
        if(position>0)
        {
            val dif= dateDif(dataArray[position].date!!.toLong(),dataArray[position-1].date!!.toLong())
            if(dif=="false")
            {
                holder.date.text=""
            }
            else{
                holder.date.text=displayDateAndTime(dataArray[position].date.toString())
            }

        }
        else{
            holder.date.text=displayDateAndTime(dataArray[position].date.toString())
        }



    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

}

class ChatViewHolder(private val view: View): RecyclerView.ViewHolder(view)
{
    val mess= view.findViewById(R.id.tvMessage) as TextView
    val pic = view.findViewById(R.id.message_img_left) as ImageView
    val date= view.findViewById(R.id.tvMessDate) as TextView
    val box = view.findViewById(R.id.message_background) as CardView

}

private fun dateDif(dateNow:Long, datePre:Long):String{
    val mNow=dateNow%100
    val hNow=(dateNow%10000-mNow)/100
    val dNow=(dateNow-dateNow%10000)/10000
    val mPre=datePre%100
    val hPre=(datePre%10000-mPre)/100
    val dPre=(datePre-datePre%10000)/10000
    val minDif= (hNow*60+mNow)-(hPre*60-mPre)
    Log.d("MIN_DIF","$minDif")
    Log.d("DATE_NOW","$dNow")
    Log.d("DATE_PRE","$dPre")
    if(dNow!=dPre || minDif>60) return dateNow.toString()
    return "false"
}





