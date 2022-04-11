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
            .getReference("events")

        binding.notRw.layoutManager= LinearLayoutManager(activity)
        binding.notRw.adapter



        val listOfEvents: ArrayList<String> = ArrayList()

        firebase.child("users").child("${user.uid}").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    val newRow =user.uid + i.getValue(String::class.java)
                    listOfEvents.add(newRow)
                    Log.d("EVENTS_TEST",newRow)
                }
                Log.d("ARRAY_TEST",listOfEvents.toString())
                setUpData(listOfEvents,firebase)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return binding.root
    }
    private fun notiSetUpAdapter(arrayData: ArrayList<WaitingClass>){
        binding.notRw.adapter = NotiAdapter(arrayData)
    }


    fun setUpData(list: ArrayList<String>, ref: DatabaseReference){
        val listOfWaiting: ArrayList<WaitingClass> = ArrayList()
        for((counter, a) in list.withIndex())
        {
            Log.d("LOOP_TEST",a)
            ref.child("all").child(a).child("waiting").addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("DATA_CHANGE","WORK")
                    for(b in snapshot.children) {
                        val newPerson = b.getValue(String::class.java)
                        val newRow = WaitingClass(newPerson.toString(), a)
                        listOfWaiting.add(newRow)
                        Log.d("WAITING_LIST", listOfWaiting.size.toString())
                        Log.d("Counter",counter.toString())
                        Log.d("List_size",list.size.toString())
                        if(counter==list.size-2)
                        {
                            Log.d("SIZE_OF_LIST",listOfWaiting.size.toString())
                            notiSetUpAdapter(listOfWaiting)
                        }
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {Log.d("onCancelled", "active")}
            })

        }


    }
}

