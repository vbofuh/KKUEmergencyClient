// java/com/example/sos/MainActivity.kt
package com.example.sos

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sos.login.LoginFragment
import com.example.sos.register.Register1Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        // Load LoginFragment initially
        loadFragment(LoginFragment())

        loginButton.setOnClickListener {
            // Check if LoginFragment is already in backstack
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack(LoginFragment::class.java.simpleName, 0)
            } else {
                loadFragment(LoginFragment())
            }
            loginButton.isEnabled = false
            loginButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.myred)
            registerButton.isEnabled = true
            registerButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.mylightred)
        }

        registerButton.setOnClickListener {
            loadFragment(Register1Fragment())
            registerButton.isEnabled = false
            registerButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.myred)
            loginButton.isEnabled = true
            loginButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.mylightred)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment::class.java.simpleName) // Use fragment's class name as tag
        transaction.commit()
    }
}