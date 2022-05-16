package com.example.profeska

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profeska.databinding.FragmentChatPickBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatPickFragment : Fragment() {

    private lateinit var listOfItems:ArrayList<String>
    private var _binding: FragmentChatPickBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val id=FirebaseAuth.getInstance().uid
        _binding = FragmentChatPickBinding.inflate(layoutInflater, container, false)

        binding.rvChatPicker.layoutManager= LinearLayoutManager(activity)
        binding.rvChatPicker.adapter

        val ref= FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users").child("$id").child("accepted")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfItems =ArrayList()
                for(i in snapshot.children){
                    val newRow = i.value.toString()
                    Log.d("ROW_TEST", "$i")
                    listOfItems.add(newRow!!)
                }
                Log.d("ARRAY_TEST", listOfItems.toString())
                setUpChatPickAdapter(listOfItems)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })




        return binding.root
    }


    private fun setUpChatPickAdapter(arrayData: ArrayList<String>){
        binding.rvChatPicker.adapter = ChatPickAdapter(arrayData)
        Log.d("setUp","${arrayData.size}")
    }

}

