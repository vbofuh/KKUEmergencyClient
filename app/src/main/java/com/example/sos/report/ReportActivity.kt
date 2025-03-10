package com.example.sos.report

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sos.R
import com.example.sos.databinding.ActivityReportBinding
import com.example.sos.viewmodel.ReportViewModel

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding
    private lateinit var reportViewModel: ReportViewModel
    private var incidentType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // รับค่า incidentType ที่ส่งมาจากหน้า Home
        incidentType = intent.getStringExtra("incidentType") ?: ""

        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        // แสดงข้อมูลผู้แจ้งเหตุ
        reportViewModel.getCurrentUser().observe(this) { user ->
            binding.tvReporterName.text = "${user.firstName} ${user.lastName} (ผู้แจ้ง)"
        }

        // ตั้งค่า spinner สำหรับความเกี่ยวข้องกับผู้ประสบเหตุ
        val relationOptions = arrayOf("ผู้ประสบเหตุ", "ผู้เห็นเหตุการณ์", "เพื่อนผู้ประสบเหตุ")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, relationOptions)
        binding.spinnerRelation.adapter = adapter

        // ตั้งค่าปุ่มยืนยัน
        binding.btnSubmit.setOnClickListener {
            val location = binding.etLocation.text.toString()
            val additionalInfo = binding.etAdditionalInfo.text.toString()
            val relation = binding.spinnerRelation.selectedItem.toString()

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