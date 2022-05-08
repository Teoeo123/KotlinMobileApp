package com.example.profeska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.profeska.databinding.ActivityPartyAcceptBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

class PartyAcceptActivity : AppCompatActivity() {

    private lateinit var listOfWaiting: ArrayList<WaitingClass>
    private lateinit var binding: ActivityPartyAcceptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPartyAcceptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.partyAcceptRec.layoutManager= LinearLayoutManager(this)

        var value :String?=null;
        val extras = intent.extras
        if (extras != null) {
            value = extras.getString("id")
        }
        Log.d("PASS_TEST",value.toString())
        val ref= FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("events").child("all").child(value.toString())

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfWaiting= ArrayList()
                for(i in snapshot.children)
                {
                    val newRow=WaitingClass(i.toString(),value)
                    listOfWaiting.add(newRow)
                }
                notiSetUpAdapter(listOfWaiting)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun notiSetUpAdapter(arrayData: ArrayList<WaitingClass>){
        binding.partyAcceptRec.adapter = NotiAdapter(arrayData)
    }
}