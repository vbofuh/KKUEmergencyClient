package com.example.sos.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sos.chat.ChatActivity
import com.example.sos.databinding.ActivitySummaryBinding
import com.example.sos.viewmodel.SummaryViewModel

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding
    private lateinit var summaryViewModel: SummaryViewModel
    private var incidentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        incidentId = intent.getStringExtra("incidentId") ?: ""

        summaryViewModel = ViewModelProvider(this).get(SummaryViewModel::class.java)

        summaryViewModel.getIncidentDetails(incidentId).observe(this) { incident ->
            binding.tvStatus.text = "สถานะ: ${incident.status}"
            binding.tvReporterName.text = "${incident.reporterName}"
            binding.tvRelation.text = "เกี่ยวข้องเป็น: ${incident.relationToVictim}"
            binding.tvLocation.text = "สถานที่: ${incident.location}"
            binding.tvAdditionalInfo.text = "ข้อมูลเพิ่มเติม: ${incident.additionalInfo}"
        }

        // ตั้งค่าปุ่มแชท
        binding.btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatId", incidentId)
            startActivity(intent)
        }

        // ตั้งค่าปุ่มโทร
        binding.btnCall.setOnClickListener {
            summaryViewModel.getStaffPhone(incidentId).observe(this) { phone ->
                if (phone.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phone")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "ยังไม่มีเจ้าหน้าที่รับเรื่อง", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}