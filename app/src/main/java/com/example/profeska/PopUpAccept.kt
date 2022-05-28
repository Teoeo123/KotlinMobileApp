package com.example.profeska

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profeska.databinding.ActivityPopUpAcceptBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.fragment.app.Fragment

class PopUpAccept : AppCompatActivity() {

    private lateinit var listOfWaiting: ArrayList<WaitingClass>
    private lateinit var binding: ActivityPopUpAcceptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_accept)
        binding = ActivityPopUpAcceptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)

        var value :String?=null;
        val extras = intent.extras
        if (extras != null) {
            value = extras.getString("id")
        }
        Log.d("PASS_TEST",value.toString())
        val ref=FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("events").child("all").child(value.toString()).child("waiting")

        binding.popNotiAdapt.layoutManager= LinearLayoutManager(this)

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfWaiting= ArrayList()
                listOfWaiting.clear()
                for(i in snapshot.children)
                {
                    Log.d("ID_TEST",i.toString())
                    val newRow=WaitingClass(i.value.toString(),value)
                    listOfWaiting.add(newRow)
                }
                if(listOfWaiting.size==0)
                {
                    binding.txIsNo.alpha=0.5.toFloat()
                }
                else {
                    notiSetUpAdapter(listOfWaiting)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        val alpha = 0 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.BLACK, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            binding.popupWindowBackground.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()




        binding.popupWindowViewWithBorder.alpha = 0f
        binding.popupWindowViewWithBorder.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        binding.popupWindowButton.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 0 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.BLACK)
        colorAnimation.duration = 300 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            binding.popupWindowBackground.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        binding.popupWindowViewWithBorder.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }

    private fun notiSetUpAdapter(arrayData: ArrayList<WaitingClass>){
        binding.popNotiAdapt.adapter = NotiAdapter(arrayData)
    }

}