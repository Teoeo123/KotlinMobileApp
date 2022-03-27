package com.example.profeska

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.UriMatcher
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import com.example.profeska.databinding.ActivityProfilEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class ProfilEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilEditBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseAuth
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profil_edit)


        binding = ActivityProfilEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)
        var plec ="inne"

        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        user = FirebaseAuth.getInstance()
        myRef = firebase.getReference("users")
        val id=user.uid

        binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
                val rb = findViewById<RadioButton>(checkedId)
                if(rb!=null) {
                    plec = rb.text.toString()
                }
        }

        binding.btnProf.setOnClickListener{
            val name = binding.etUserName.text.toString()
            val sName = binding.etUserSName.text.toString()
            val number = binding.etUserPhon.text.toString()
            val city = binding.etUserCity.text.toString()
            val firebaseInput = DatabaseRow(name,sName,number,city,plec)
            myRef.child(id.toString()).setValue(firebaseInput)
            startActivity(Intent(this,LogoutActivity::class.java))
            if(imageUri!=null) {
                uploadImage()
            }
        }

        binding.profPicEdit.setOnClickListener{
            Log.d("Zdjecie","dziala")
            selectImage()
        }

    }

    private  fun uploadImage(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Upload Image")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val fileName = user.uid
        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$fileName")
        storageRef.putFile((imageUri))
            .addOnSuccessListener {

                binding.profPicEdit.setImageURI(null)
                Log.d("ADDING","SUCCESS")
            }
            .addOnFailureListener{
                Log.d("ADDING","REJECTED")
            }

    }

    private  fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            binding.profPicEdit.setImageURI(imageUri)
            Log.d("TEST","$imageUri")
        }
    }

}