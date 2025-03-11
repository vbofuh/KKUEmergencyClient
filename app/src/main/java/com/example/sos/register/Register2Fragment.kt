package com.example.sos.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sos.MainActivity
import com.example.sos.R
import com.example.sos.home.HomeActivity
import com.example.sos.viewmodel.RegisterViewModel
import com.example.sos.login.LoginFragment
import androidx.core.content.ContextCompat

class Register2Fragment : Fragment() {

    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var backButton: Button
    private lateinit var confirmButton: Button
    private lateinit var registerViewModel: RegisterViewModel

    private var email: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var phoneNumber: String = ""
    private var role: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register2, container, false)

        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        backButton = view.findViewById(R.id.backButton)
        confirmButton = view.findViewById(R.id.confirmButton)

        // รับข้อมูลจาก arguments
        arguments?.let {
            email = it.getString("email", "")
            firstName = it.getString("firstName", "")
            lastName = it.getString("lastName", "")
            phoneNumber = it.getString("phone", "")
            role = it.getString("role", "")

            Log.d("Register2", "Received data: $email, $firstName, $lastName, $phoneNumber, $role")
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // ย้อนกลับไป Register1Fragment
        }

        // ตรวจสอบผลลัพธ์การลงทะเบียน
        // ตรวจสอบผลลัพธ์การลงทะเบียน
        registerViewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            if (result.success) {
                Toast.makeText(requireContext(), "ลงทะเบียนสำเร็จ", Toast.LENGTH_SHORT).show()

                // แทนที่จะไปที่ HomeActivity ให้กลับไปที่หน้าล็อกอิน
                val loginFragment = LoginFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
                    .commit()

                // อัปเดตปุ่มใน MainActivity (หากต้องการ)
                (activity as? MainActivity)?.let { mainActivity ->
                    // ปรับปรุงสถานะปุ่มเพื่อให้ Login มีสีเข้มและ Register เป็นสีอ่อน
                    val loginButton = mainActivity.findViewById<Button>(R.id.loginButton)
                    val registerButton = mainActivity.findViewById<Button>(R.id.registerButton)

                    loginButton.isEnabled = false
                    loginButton.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.myred)
                    registerButton.isEnabled = true
                    registerButton.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.mylightred)
                }
            } else {
                Toast.makeText(requireContext(), result.errorMessage ?: "การลงทะเบียนล้มเหลว", Toast.LENGTH_SHORT).show()
            }
        }

        confirmButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (isValidRegister2(password, confirmPassword)) {
                // ลงทะเบียนผู้ใช้ผ่าน ViewModel
                registerViewModel.registerUser(email, password, firstName, lastName, phoneNumber, role)
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