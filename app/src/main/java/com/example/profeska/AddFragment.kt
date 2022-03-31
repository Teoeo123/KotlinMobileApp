package com.example.profeska

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.profeska.databinding.FragmentAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class AddFragment : Fragment(R.layout.fragment_add) {


    private var _binding: FragmentAddBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var myRef: DatabaseReference
    private lateinit var allRef: DatabaseReference
    private lateinit var user: FirebaseAuth
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        user = FirebaseAuth.getInstance()
        myRef = firebase.getReference("events").child("users")
        allRef = firebase.getReference("events").child("all")


        binding.imgEdit.setOnClickListener{
            selectImage()
        }

        binding.btnzatw.setOnClickListener {
            val n=sendToBase()
            if(imageUri!=null) uploadImage(n)
            startActivity(Intent(activity,LogoutActivity::class.java))
        }

        binding.btnodr.setOnClickListener {
            startActivity(Intent(activity,LogoutActivity::class.java))
        }









        return binding.root

    }


    private fun sendToBase():String
    {
        val id=user.uid
        val name=binding.eventName.text.toString()
        val desc=binding.eventDes.text.toString()
        val slots=binding.eventSlots.text.toString().toInt()
        val city=binding.eventCity.text.toString()
        val street=binding.eventStreet.text.toString()
        val nr=binding.eventNr.text.toString()
        val photo="$id$name"
        val data=DatabaseEvent(name,desc,slots,city,street,nr,photo)
        myRef.child("$id").child("$name").setValue(data)
        allRef.child("$photo").setValue(data)
        return name
    }

    private  fun uploadImage(name:String){
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Upload Image")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val fileName = user.uid
        val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("events/$fileName$name")
        storageRef.putFile((imageUri))
            .addOnSuccessListener {

                binding.imgEdit.setImageURI(null)
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

        if (requestCode ==100 && resultCode == AppCompatActivity.RESULT_OK){
            imageUri = data?.data!!
            binding.imgEdit.setImageURI(imageUri)
            Log.d("TEST","$imageUri")
        }
    }





}
