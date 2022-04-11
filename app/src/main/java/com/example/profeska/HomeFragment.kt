package com.example.profeska

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import com.example.profeska.databinding.FragmentHomeBinding
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var myRef: DatabaseReference
    private lateinit var listOfItems: ArrayList<DatabaseEvent>
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)



        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = firebase.getReference("events").child("all")


        binding.recyclerView.layoutManager= LinearLayoutManager(activity)

        binding.recyclerView.adapter

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfItems =ArrayList()
                for(i in snapshot.children){
                    val newRow = i.getValue(DatabaseEvent::class.java)
                    listOfItems.add(newRow!!)
                }
                Log.d("ARRAY_TEST", listOfItems.size.toString())
                setUpAdapter(listOfItems)

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        return binding.root

    }

    private fun setUpAdapter(arrayData: ArrayList<DatabaseEvent>){
        binding.recyclerView.adapter = MyAdapter(arrayData)
    }



    }





