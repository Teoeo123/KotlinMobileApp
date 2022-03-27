package com.example.profeska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.profeska.databinding.ActivityEventsBinding
import com.example.profeska.databinding.ActivityLogoutBinding
import com.google.firebase.database.*

class EventsActivity : AppCompatActivity() {

    private lateinit var myRef: DatabaseReference
    private lateinit var listOfItems: ArrayList<DatabaseEvent>

    private lateinit var binding: ActivityEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = firebase.getReference("events").child("all")

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this,EventEditActivity::class.java))
        }
        binding.recyclerView.layoutManager=LinearLayoutManager(this)

        binding.recyclerView.adapter

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfItems =ArrayList()
                for(i in snapshot.children){
                    val newRow = i.getValue(DatabaseEvent::class.java)
                    listOfItems.add(newRow!!)
                }
                setUpAdapter(listOfItems)

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun setUpAdapter(arrayData: ArrayList<DatabaseEvent>){
        binding.recyclerView.adapter = MyAdapter(arrayData)
    }
}