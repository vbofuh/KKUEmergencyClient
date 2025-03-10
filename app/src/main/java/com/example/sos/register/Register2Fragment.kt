//java/com/example/sos/register/Register2Fragment.kt
package com.example.sos.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sos.MainActivity
import com.example.sos.login.LoginFragment
import com.example.sos.R

class Register2Fragment : Fragment() {

    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var backButton: Button
    private lateinit var confirmButton: Button
    private var email: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register2, container, false)

        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        backButton = view.findViewById(R.id.backButton)
        confirmButton = view.findViewById(R.id.confirmButton)

        // Get email from arguments
        arguments?.let {
            email = it.getString("email", "")
        }


        backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Go back to Register1Fragment
        }

        confirmButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (isValidRegister2(password, confirmPassword)) {
                Toast.makeText(requireContext(), "Welcome, $email", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                requireActivity().finish() // ปิด Activity ปัจจุบัน
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "รหัสผ่านไม่ตรงกัน หรือไม่ถูกต้อง", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun isValidRegister2(password: String, confirmPassword: String): Boolean {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}\$".toRegex()
        return password == confirmPassword && passwordRegex.matches(password)
    }
}