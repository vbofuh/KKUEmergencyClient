package com.example.sos.report

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sos.R
import com.example.sos.home.HomeActivity
import com.example.sos.viewmodel.ReportViewModel

class ReportActivity : AppCompatActivity() {

    private lateinit var tvReporterName: TextView
    private lateinit var spinnerRelation: Spinner
    private lateinit var etLocation: EditText
    private lateinit var etAdditionalInfo: EditText
    private lateinit var btnSubmit: Button
    private lateinit var backButton: TextView

    private lateinit var reportViewModel: ReportViewModel
    private var incidentType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // แก้ไขให้ใช้ชื่อไฟล์ layout ที่มีอยู่จริงในโปรเจ็ค
        setContentView(R.layout.activity_report)

        // ผูกตัวแปรกับ views
        tvReporterName = findViewById(R.id.tvReporterName)
        spinnerRelation = findViewById(R.id.spinnerRelation)
        etLocation = findViewById(R.id.etLocation)
        etAdditionalInfo = findViewById(R.id.etAdditionalInfo)
        btnSubmit = findViewById(R.id.btnSubmit)
        backButton = findViewById(R.id.backButton)

        // รับค่า incidentType ที่ส่งมาจากปุ่มที่กดในหน้าแรก
        incidentType = intent.getStringExtra("incidentType") ?: ""

        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        // แสดงข้อมูลผู้แจ้งเหตุ
        reportViewModel.getCurrentUser().observe(this) { user ->
            tvReporterName.text = "${user.firstName} ${user.lastName}"
        }

        // ตั้งค่า spinner สำหรับความเกี่ยวข้องกับผู้ประสบเหตุ
        val relationOptions = arrayOf("ผู้ประสบเหตุ", "ผู้เห็นเหตุการณ์", "เพื่อนผู้ประสบเหตุ")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, relationOptions)
        spinnerRelation.adapter = adapter

        // ตั้งค่าปุ่มย้อนกลับให้กลับไปหน้า Home
        backButton.setOnClickListener {
            // ใช้ Intent เพื่อกลับไปยังหน้า HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            // เพิ่ม FLAG_ACTIVITY_CLEAR_TOP เพื่อล้าง activity stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish() // ปิด ReportActivity นี้
        }

        // ตั้งค่าปุ่มยืนยัน
        btnSubmit.setOnClickListener {
            val location = etLocation.text.toString()
            val additionalInfo = etAdditionalInfo.text.toString()
            val relation = spinnerRelation.selectedItem.toString()

            if (location.isBlank()) {
                Toast.makeText(this, "กรุณาระบุสถานที่เกิดเหตุ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            reportViewModel.reportIncident(incidentType, location, relation, additionalInfo)
                .observe(this) { result ->
                    if (result.success) {
                        // ไปยังหน้าสรุปการแจ้งเหตุ
                        val intent = Intent(this, SummaryActivity::class.java)
                        intent.putExtra("incidentId", result.incidentId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, result.message ?: "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}