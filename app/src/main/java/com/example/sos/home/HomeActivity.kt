//java/com/example/sos/home/HomeActivity.kt
package com.example.sos.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sos.R
import com.example.sos.account.AccountFragment
import com.example.sos.message.MessageFragment
import com.example.sos.pending.PendingFragment
import com.nafis.bottomnavigation.NafisBottomNavigation

class HomeActivity : AppCompatActivity() {

    private val ID_home = 1
    private val ID_pending = 2
    private val ID_message = 3
    private val ID_account = 4

    private val homeFragment = HomeFragment()
    private val pendingFragment = PendingFragment()
    private val messageFragment = MessageFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (savedInstanceState == null) {
            setCurrentFragment(homeFragment)
        }

        val bottomNavigation = findViewById<NafisBottomNavigation>(R.id.bottomNavigation)

        bottomNavigation.add(NafisBottomNavigation.Model(ID_home, R.drawable.ic_home))
        bottomNavigation.add(NafisBottomNavigation.Model(ID_pending, R.drawable.ic_pending))
        bottomNavigation.add(NafisBottomNavigation.Model(ID_message, R.drawable.ic_message))
        bottomNavigation.add(NafisBottomNavigation.Model(ID_account, R.drawable.ic_account))

        bottomNavigation.setOnClickMenuListener { model ->
            when (model.id) {
                ID_home -> setCurrentFragment(homeFragment)
                ID_pending -> setCurrentFragment(pendingFragment)
                ID_message -> setCurrentFragment(messageFragment)
                ID_account -> setCurrentFragment(accountFragment)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    // ฟังก์ชันเปลี่ยน Fragment แบบป้องกันซ้ำซ้อน + Animation
    private fun setCurrentFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != fragment) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.fade_in,  // Animation เวลาเข้า
                    android.R.anim.fade_out  // Animation เวลาออก
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}