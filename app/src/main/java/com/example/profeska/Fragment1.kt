package com.example.profeska



import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast

import androidx.fragment.app.Fragment
import com.example.profeska.databinding.Fragment1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class Fragment1 : Fragment(R.layout.fragment1) {




        private var _binding: Fragment1Binding? = null
        private val binding
        get() = _binding!!

   private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseAuth
        override fun onCreateView(
            inflater: LayoutInflater,container: ViewGroup?,
            savedInstanceState: Bundle?


        ): View? {
            _binding = Fragment1Binding.inflate(layoutInflater, container, false)




            user = FirebaseAuth.getInstance()
            val id=user.uid
            readUData(id)

            val imageName = "basic-profil.png"
            val storageRefIfAdd= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$id")
            val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$imageName")
            val localFile = File.createTempFile("temp","png")

            storageRefIfAdd.getFile(localFile).addOnSuccessListener {
                val bitmap =BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("ProfPic","Success")
                binding.imgProfPic.setImageBitmap(bitmap)
            }.addOnFailureListener{
                storageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap =BitmapFactory.decodeFile(localFile.absolutePath)
                    Log.d("ProfPic","Success")
                    binding.imgProfPic.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnEditProfP.setOnClickListener{
                startActivity(Intent(activity,ProfilEditActivity::class.java))
            }

            binding.btnLogout.setOnClickListener{
                user.signOut()
                startActivity(Intent(activity,MainActivity::class.java))
                Toast.makeText(activity, "Wylogowano", Toast.LENGTH_SHORT).show()


            }








            return binding.root
        }

    private fun readUData(user: String?) {


        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = firebase.getReference("users").child(user.toString())
        myRef.get().addOnSuccessListener {

            if(it.exists()){
                binding.txtImieP.text="${it.child("name").value.toString()} ${it.child("sname").value.toString()}"
               // binding.txtNazwiP.text="Nazwisko: ${it.child("sname").value.toString()}"
                binding.txtMiastP.text="${it.child("city").value.toString()}"
                binding.txtNumP.text="${it.child("number").value.toString()}"
                binding.txtPlecP.text="${it.child("sex").value.toString()}"


                if(binding.txtPlecP.text.contains("Kobieta")){

                    binding.txtPlecP.setCompoundDrawablesRelativeWithIntrinsicBounds(

                        R.drawable.ic_venus,
                            0,
                                0,
                        0

                    )
                }




             
            }
            else{
                Toast.makeText(activity,"Nie uzupełniłes profilu",Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity,ProfilEditActivity::class.java))
            }
        }
    }

    }











