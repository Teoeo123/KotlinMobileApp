package com.example.profeska

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.profeska.databinding.ActivityEventShowBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import java.io.File

class EventShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventShowBinding
    private lateinit var evRef:DatabaseReference
    private lateinit var usRef:DatabaseReference
    private lateinit var user: FirebaseAuth
    private lateinit var listOfItems: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val value :String?
        val extras = intent.extras
        if (extras != null) {
             value = extras.getString("id")
        }
        else
        {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,LogoutActivity::class.java))
            return
        }
        fullScreen(window)
        user= FirebaseAuth.getInstance()

        val firebase= FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        evRef= firebase.getReference("events").child("all").child("$value")
        evRef.get().addOnSuccessListener {
            val evName=it.child("name").value.toString()
            val usId = value?.removeSuffix(evName).toString()
            Log.d("EV_NAME",evName)
            binding.tvNameEvSh.text=evName
            binding.tvDesEvSh.text=it.child("description").value.toString()
            usRef=firebase.getReference("users").child(usId)

            usRef.get().addOnSuccessListener {
                binding.tvUsNamEvSh.text="${it.child("name").value.toString()} ${it.child("sname").value.toString()}"

                val imageName = "basic-profil.png"
                val storageRefIfAdd= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$usId")
                val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$imageName")
                val localFile = File.createTempFile("temp","png")

                storageRefIfAdd.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    Log.d("PROF_PIC","Success")
                    binding.profPicEvSh.setImageBitmap(bitmap)
                }.addOnFailureListener{
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        Log.d("PROF_PIC","Success")
                        binding.profPicEvSh.setImageBitmap(bitmap)
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                binding.tvUsNamEvSh.setText("unknown")
            }

        }.addOnFailureListener {
                startActivity(Intent(this,LogoutActivity::class.java))
        }
        val id=user.uid

        binding.UserAdapter.layoutManager= LinearLayoutManager(this)
        binding.UserAdapter.adapter


        val uRef=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app").getReference("events")
            .child("all").child("$value").child("accepted")

        listOfItems= ArrayList()
        uRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    val newRow=i.value.toString()
                    listOfItems.add(newRow)
                    Log.d("User",newRow)
                }
                 userSetUpAdapter(listOfItems)
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        binding.btnAplEvSh.setOnClickListener {
            val uUid = "${user.uid}"
            evRef.child("waiting").child(uUid).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(!snapshot.exists())
                    {
                        evRef.child("accepted").child(uUid).addValueEventListener(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(!snapshot.exists())
                                {
                                    evRef.child("rejected").child(uUid).addValueEventListener(object: ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if(!snapshot.exists())
                                            {
                                                Log.d("TEST","Dziala")
                                                if(!value.toString().contains(uUid)){
                                                    binding.btnAplEvSh.isClickable=false
                                                    binding.btnAplEvSh.alpha=0.25.toFloat()
                                                    evRef.child("waiting").child("$id").setValue(id).addOnFailureListener {

                                                    }
                                                }
                                                else{
                                                    binding.btnAplEvSh.isClickable=false
                                                    binding.btnAplEvSh.alpha=0.25.toFloat()
                                                }

                                            }
                                            else{
                                                binding.btnAplEvSh.isClickable=false
                                                binding.btnAplEvSh.alpha=0.25.toFloat()
                                            }
                                        }
                                        override fun onCancelled(databaseError: DatabaseError) {Log.d("onCancelled", "active")}
                                    })
                                }
                                else{
                                    binding.btnAplEvSh.isClickable=false
                                    binding.btnAplEvSh.alpha=0.25.toFloat()
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {Log.d("onCancelled", "active")}
                        })
                    }
                    else{
                        binding.btnAplEvSh.isClickable=false
                        binding.btnAplEvSh.alpha=0.25.toFloat()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {Log.d("onCancelled", "active")}
            })
        }


    }
    private fun userSetUpAdapter(arrayData: ArrayList<String>){
        binding.UserAdapter.adapter = UserAdapter(arrayData)
    }


}

