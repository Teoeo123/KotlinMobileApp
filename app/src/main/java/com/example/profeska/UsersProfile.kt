package com.example.profeska

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.profeska.databinding.ActivityUsersProfileBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UsersProfile : AppCompatActivity() {

    private lateinit var binding: ActivityUsersProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)

        val value :String?
        val extras = intent.extras
        if (extras != null) {
            value = extras.getString("id")
        }
        else
        {
            Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,LogoutActivity::class.java))
            return
        }

        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$value")

        val localFile = File.createTempFile("temp","png")

        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.usrProfPic.setImageBitmap(bitmap)
        }

        val ref=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users").child("$value")
        ref.get().addOnSuccessListener {
            val name= it.child("name").value.toString()
            val sname= it.child("sname").value.toString()
            val phon= it.child("number").value.toString()
            val sex= it.child("sex").value.toString()
            val city= it.child("city").value.toString()

            binding.usrImieP.text="$name $sname"
            binding.usrMiastP.text= city
            binding.usrNumP.text = phon
            binding.usrMiastP.text = city
            binding.usrPlecP.text= sex


            if(binding.usrPlecP.text.contains("Kobieta")){

                binding.usrPlecP.setCompoundDrawablesRelativeWithIntrinsicBounds(

                    R.drawable.ic_venus,
                    0,
                    0,
                    0

                )
            }

            if(binding.usrPlecP.text.contains("Inne")){
                binding.usrPlecP.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_other,
                    0,
                    0,
                    0
                )
            }


            if(binding.usrNumP.text.contains("420")){
                binding.usrNumP.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_weed,
                    0,
                    0,
                    0
                )
            }



        }

        binding.btnBack.setOnClickListener{
            finish()
        }



    }
}