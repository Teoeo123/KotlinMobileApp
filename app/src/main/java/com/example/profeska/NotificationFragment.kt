package com.example.profeska

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profeska.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var user: FirebaseAuth
    private var _binding: FragmentNotificationBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)

        user= FirebaseAuth.getInstance()
        val firebase=FirebaseDatabase
            .getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("events").child("all")

        binding.notRw.layoutManager= LinearLayoutManager(activity)
        binding.notRw.adapter



        val listOfEvents: ArrayList<DatabaseEvent> = ArrayList()

        firebase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfEvents.clear()
                for(i in snapshot.children){
                    if(i.child("photo").value.toString().contains("${user.uid}")) {
                        val newRow = i.getValue(DatabaseEvent::class.java)
                        listOfEvents.add(newRow!!)
                    }

                }
                Log.d("ARRAY_TEST", listOfEvents.size.toString())
                notiSetUpAdapter(listOfEvents)

            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return binding.root
    }
    private fun notiSetUpAdapter(arrayData: ArrayList<DatabaseEvent>){
        binding.notRw.adapter = YourEventsAdapter(arrayData)
    }


}

