package com.example.profeska

import android.app.ProgressDialog.show
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.profeska.databinding.ActivityEventShowBinding.inflate
import com.example.profeska.databinding.ActivityPopUpAcceptBinding
import com.google.android.gms.common.SupportErrorDialogFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.File
import kotlin.coroutines.coroutineContext

class YourEventsAdapter(private val dataArray: ArrayList<DatabaseEvent>): RecyclerView.Adapter<YourEventsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourEventsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventRow = layoutInflater.inflate(R.layout.my_event_row_layout, parent, false)
        return YourEventsViewHolder(eventRow)
    }

    override fun onBindViewHolder(holder: YourEventsViewHolder, position: Int) {
        val photo = dataArray[holder.adapterPosition].photo
        holder.name.text=dataArray[holder.adapterPosition].name
        holder.des.text=dataArray[holder.adapterPosition].description
        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("events/$photo")
        val localFile = File.createTempFile("temp","png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.pic.setImageBitmap(bitmap)
        }
        val ref=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app").getReference("events").child("all")
            .child(dataArray[holder.adapterPosition].photo.toString()).child("waiting")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var counter=0
                for(i in snapshot.children.withIndex()){
                    counter++
                }
                if(counter>=0){
                    val o:Float=0.toFloat()
                    holder.count.alpha = o
                }

                holder.count.text=counter.toString()



            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        holder.click.setOnClickListener{
            val myIntent= Intent(holder.itemView.context,PopUpAccept::class.java)
            myIntent.putExtra("id",photo)
            startActivity(holder.itemView.context,myIntent,null)
        }

    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

}

class YourEventsViewHolder(private val view: View): RecyclerView.ViewHolder(view)
{
    val name=view.findViewById(R.id.MyName) as TextView
    val des=view.findViewById(R.id.MyDes) as TextView
    val pic=view.findViewById(R.id.myEventImg) as ImageView
    val click=view.findViewById(R.id.myRelativeLayout) as ConstraintLayout
    val count=view.findViewById(R.id.NotiCount) as TextView
    val id=FirebaseAuth.getInstance()
}