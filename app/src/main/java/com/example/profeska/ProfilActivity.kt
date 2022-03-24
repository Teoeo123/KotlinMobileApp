package com.example.profeska

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.profeska.databinding.ActivityProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profil)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)
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
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEditProfP.setOnClickListener {
            startActivity(Intent(this,ProfilEditActivity::class.java))
        }





    }

    @SuppressLint("SetTextI18n")
    private fun readUData(user:String?){

        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = firebase.getReference("users").child(user.toString())
        myRef.get().addOnSuccessListener {

            if(it.exists()){
                binding.txtImieP.text="Imie: ${it.child("name").value.toString()}"
                binding.txtNazwiP.text="Nazwisko: ${it.child("sname").value.toString()}"
                binding.txtMiastP.text="Telefon: ${it.child("city").value.toString()}"
                binding.txtNumP.text="Miasto: ${it.child("number").value.toString()}"
                binding.txtPlecP.text="Płeć: ${it.child("sex").value.toString()}"
            }
            else{
                Toast.makeText(this,"Nie uzupełniłes profilu",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,ProfilEditActivity::class.java))
            }
        }
    }
}