package com.example.profeska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.profeska.databinding.ActivityLogoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text
import kotlin.system.exitProcess

class LogoutActivity : AppCompatActivity() {


    private lateinit var notificationsBadges : View
    private var count: Int = 0
    private lateinit var userPartyList: ArrayList<String>

    private lateinit var binding: ActivityLogoutBinding
    private lateinit var user: FirebaseAuth
    lateinit var backToast:Toast
    private var backPressedTime:Long = 0
    val homeFragment = HomeFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)
        user = FirebaseAuth.getInstance()


        userPartyList=ArrayList()
        val userRef =FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users").child("${user.uid}")
        val ref= FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("events")
        Log.d("UID","${user.uid}")
        userRef.get().addOnSuccessListener{
            Log.d("IT","$it")
            if(it.value.toString()=="null")
                startActivity(Intent(this,ProfilEditActivity::class.java))
        }

        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                count=0
                for (i in snapshot.child("users").child("${user.uid}").children)
                {
                    Log.d("I_SNAP","${user.uid}${i.value}")
                    for(j in snapshot.child("all").child("${user.uid}${i.value}").child("waiting").children)
                    {
                        Log.d("J_SNAP","${j.value}")
                        count++
                    }
                }
                updateBadgeCount(count)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })





        val profileFragment = Fragment1()
        val addFragment = AddFragment()
        val notificationFragment = NotificationFragment()
        val chatPickFragment =ChatPickFragment()

        makeCurrentFragment(homeFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_profile -> makeCurrentFragment(profileFragment)
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_add -> makeCurrentFragment(addFragment)
                R.id.ic_noti ->makeCurrentFragment(notificationFragment)
                R.id.ic_messeges->makeCurrentFragment(chatPickFragment)
            }
            true
        }




      /*  binding.btnProfil.setOnClickListener {
            replaceFragment(Fragment1())
        }*/
    }
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer,fragment)
            commit()
        }

    private fun replaceFragment(fragment: Fragment) {

            val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {



        backToast =  Toast.makeText(this, "Wciśnij wstecz jeszcze raz, aby zamknąć aplikacje", Toast.LENGTH_LONG)
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            super.onBackPressed()
            finishAffinity()

            exitProcess(0)

        } else {
            binding.bottomNavigation.setSelectedItemId(R.id.ic_home);

            makeCurrentFragment(homeFragment)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()



    }
    private fun updateBadgeCount(count: Int = 0) {
        if (count != 0) {
            val itemView: BottomNavigationItemView? =
                binding.bottomNavigation.getChildAt(5) as? BottomNavigationItemView
            notificationsBadges = LayoutInflater.from(this)
                .inflate(R.layout.badge_text, itemView, true)

            val pic = notificationsBadges.findViewById(R.id.noti_badge) as TextView
            pic?.text = count.toString()


            binding.bottomNavigation?.addView(notificationsBadges)


        }
    }

}




