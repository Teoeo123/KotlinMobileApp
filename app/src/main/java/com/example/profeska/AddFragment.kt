package com.example.profeska

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.profeska.databinding.FragmentAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class AddFragment : Fragment(R.layout.fragment_add), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {


    private var _binding: FragmentAddBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var myRef: DatabaseReference
    private lateinit var allRef: DatabaseReference
    private lateinit var user: FirebaseAuth
    private lateinit var imageUri: Uri
    private var img: Boolean=false

    var day=0
    var month=0
    var year=0
    var hour=0
    var minute=0

    var savedDay=-1
    var savedMonth=-1
    var savedYear=-1
    var savedHour=-1
    var savedMinute=-1





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        user = FirebaseAuth.getInstance()
        myRef = firebase.getReference("events").child("users")
        allRef = firebase.getReference("events").child("all")

        pickDate()

        binding.imgEdit.setOnClickListener{
            selectImage()
            legitCheck()
        }

        binding.btnzatw.isEnabled=false
        binding.btnzatw.imageAlpha=75
        changeWatcher()

        binding.eventName.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.btnzatw.setOnClickListener {

            if(img)
            {
                val n=sendToBase()
                uploadImage(n)
                startActivity(Intent(activity,LogoutActivity::class.java))
            }
            else
            {
                selectImage()
            }

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
        val date="$savedYear${convertDate(savedMonth)}${convertDate(savedDay)}${convertDate(savedHour)}${convertDate(savedMinute)}"
        val data=DatabaseEvent(name,desc,slots,city,street,nr,photo,date)
        var events :List<DataSnapshot>
        myRef.child("$id").push().setValue(name)
        allRef.child("$photo").setValue(data)

        FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users").child("${user.uid}").child("accepted").push().setValue("$photo")

        return name
    }
    private fun convertDate(n:Int):String
    {
        var correct=""
        if(n<10){
            correct+="0$n"
        }
        else
        {
            correct+=n
        }
        return correct
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
            img=true
            Log.d("TEST","$imageUri")
        }
    }

    private fun getDateTimeCalendar(){
        val cal= Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month= cal.get(Calendar.MONTH)
        year=cal.get(Calendar.YEAR)
        hour=cal.get(Calendar.HOUR)
        minute=cal.get(Calendar.MINUTE)
    }

    private fun pickDate(){
        binding.btSetDate.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(requireContext(),this,year,month,day).show()
        }
    }


    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay=dayOfMonth
        savedMonth=month
        savedYear=year

        getDateTimeCalendar()
        TimePickerDialog(requireContext(),this,hour,minute,true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour=hourOfDay
        savedMinute=minute

        binding.tvDate.text="${convertDate(savedMonth)}/${convertDate(savedDay)}/$savedYear $savedHour:$savedMinute"
        legitCheck()
    }

    private fun legitCheck(){
        if(binding.eventName.text.isNotEmpty()
            && binding.eventDes.text.isNotEmpty()
            && binding.eventSlots.text.isNotEmpty()
            && binding.eventCity.text.isNotEmpty()
            && binding.eventStreet.text.isNotEmpty()
            && binding.eventNr.text.isNotEmpty()
            && savedMinute!=-1){
            binding.btnzatw.isEnabled=true
            binding.btnzatw.imageAlpha=255
        }
    }


    private fun changeWatcher(){
        binding.eventName.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.eventDes.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.eventSlots.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.eventCity.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.eventStreet.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.eventNr.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

    }



}
