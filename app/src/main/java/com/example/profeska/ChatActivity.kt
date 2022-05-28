package com.example.profeska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profeska.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var listOfMessages: ArrayList<MessageClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)
        val eventN :String?
        val eventId :String?
        val extras = intent.extras
        if (extras != null) {
            eventId = extras.getString("id")
            eventN=extras.getString("name")
        }
        else
        {
            Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,LogoutActivity::class.java))
            return
        }
        binding.rvChat.layoutManager= LinearLayoutManager(this)

        binding.tvEventName.text="$eventN"

        val ref=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app").getReference("chats").child(eventId.toString())
        listOfMessages= ArrayList()
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfMessages.clear()
                for(i in snapshot.children){
                    var newRow = i.getValue(MessageClass::class.java)
                    listOfMessages.add(newRow!!)
                }
                Log.d("ARRAY_TEST", listOfMessages.size.toString())
                setUpMessAdapter(listOfMessages)
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }

        }  )





        binding.chatSend.setOnClickListener {
            var mess = binding.chatInput.text
            if(mess.isNotEmpty())
            {
                sendMess(eventId.toString())
                binding.chatInput.setText("")
            }
        }

    }

    private fun sendMess(id:String)
    {
        val userId = FirebaseAuth.getInstance().uid
        val fireRef=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app").getReference("chats").child(id)
        val data = MessageClass(userId,binding.chatInput.text.toString(),getDate())
        fireRef.push().setValue(data)
    }

    private fun getDate():String{
        val cal= Calendar.getInstance()
        val y=cal.get(Calendar.YEAR)
        val mo=cal.get(Calendar.MONTH)
        val d=cal.get(Calendar.DAY_OF_MONTH)
        val h=cal.get(Calendar.HOUR)
        val min=cal.get(Calendar.MINUTE)
        return "$y${convertDate(mo)}${convertDate(d)}${convertDate(h)}${convertDate(min)}"

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

    private fun setUpMessAdapter(array:ArrayList<MessageClass>)
    {
        binding.rvChat.adapter=ChatAdapter(array)
        binding.rvChat.scrollToPosition(array.size - 1);
    }



}

