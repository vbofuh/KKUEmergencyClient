package com.example.sos.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.sos.R
import com.example.sos.guides.SurvivalGuidesActivity
import com.example.sos.home.HomeActivity
import com.example.sos.chat.ChatActivity
import com.example.sos.viewmodel.SummaryViewModel

class SummaryActivity : AppCompatActivity() {

    private lateinit var tvStatusTitle: TextView
    private lateinit var tvReporterName: TextView
    private lateinit var tvRelation: TextView
    private lateinit var tvIncidentType: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvAdditionalInfo: TextView
    private lateinit var chatButton: ImageView
    private lateinit var callButton: ImageView
    private lateinit var chatButtonContainer: androidx.cardview.widget.CardView
    private lateinit var callButtonContainer: androidx.cardview.widget.CardView
    private lateinit var backButton: TextView
    private lateinit var guideButton: Button
    private lateinit var guideCardView: CardView

    private lateinit var summaryViewModel: SummaryViewModel
    private var incidentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        // ผูกตัวแปรกับ views
        tvStatusTitle = findViewById(R.id.tvStatusTitle)
        tvReporterName = findViewById(R.id.tvReporterName)
        tvRelation = findViewById(R.id.tvRelation)
        tvIncidentType = findViewById(R.id.tvIncidentType)
        tvLocation = findViewById(R.id.tvLocation)
        tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo)
        chatButton = findViewById(R.id.chatButton)
        callButton = findViewById(R.id.callButton)
        chatButtonContainer = findViewById(R.id.chatButtonContainer)
        callButtonContainer = findViewById(R.id.callButtonContainer)
        backButton = findViewById(R.id.backButton)
        guideButton = findViewById(R.id.guideButton)
        guideCardView = findViewById(R.id.guideCardView)

        incidentId = intent.getStringExtra("incidentId") ?: ""
        if (incidentId.isEmpty()) {
            Toast.makeText(this, "ไม่พบข้อมูลการแจ้งเหตุ", Toast.LENGTH_SHORT).show()
            navigateToHome()
            return
        }

        summaryViewModel = ViewModelProvider(this).get(SummaryViewModel::class.java)

        // รับข้อมูลเหตุการณ์และแสดงผล
        summaryViewModel.getIncidentDetails(incidentId).observe(this) { incident ->
            // อัพเดทสถานะในหัวข้อ
            when (incident.status) {
                "รอรับเรื่อง" -> tvStatusTitle.text = "กำลังรอเจ้าหน้าที่รับเรื่อง"
                "เจ้าหน้าที่รับเรื่องแล้ว" -> tvStatusTitle.text = "เจ้าหน้าที่รับเรื่องแล้ว"
                "กำลังดำเนินการ" -> tvStatusTitle.text = "กำลังดำเนินการ"
                "เสร็จสิ้น" -> tvStatusTitle.text = "เสร็จสิ้น"
                else -> tvStatusTitle.text = incident.status
            }

            tvReporterName.text = incident.reporterName
            tvRelation.text = incident.relationToVictim
            tvIncidentType.text = incident.incidentType
            tvLocation.text = incident.location
            tvAdditionalInfo.text = incident.additionalInfo
        }

        // ตั้งค่าปุ่มแชท
        chatButtonContainer.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatId", incidentId)
            startActivity(intent)
        }

        chatButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatId", incidentId)
            startActivity(intent)
        }

        // ตั้งค่าปุ่มโทร
        callButtonContainer.setOnClickListener {
            // ตรวจสอบว่ามีเบอร์โทรเพิ่มเติมที่ระบุในข้อมูลเพิ่มเติมหรือไม่
            val additionalPhoneNumber = extractPhoneNumber(tvAdditionalInfo.text.toString())

            if (additionalPhoneNumber.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$additionalPhoneNumber")
                startActivity(intent)
            } else {
                // ถ้าไม่มีเบอร์โทรในข้อมูลเพิ่มเติม ให้ใช้เบอร์ของเจ้าหน้าที่
                summaryViewModel.getStaffPhone(incidentId).observe(this) { phone ->
                    if (phone.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$phone")
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "ไม่พบเบอร์โทรศัพท์สำหรับติดต่อ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        callButton.setOnClickListener {
            callButtonContainer.performClick()
        }

        // ตั้งค่าปุ่มย้อนกลับ
        backButton.setOnClickListener {
            navigateToHome()
        }

        // ตั้งค่าปุ่มดูคู่มือเอาตัวรอด
        guideButton.setOnClickListener {
            val intent = Intent(this, SurvivalGuidesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    /**
     * ดึงเบอร์โทรศัพท์จากข้อความที่กรอกในข้อมูลเพิ่มเติม
     */
    private fun extractPhoneNumber(text: String): String {
        // รูปแบบของเบอร์โทรศัพท์ในประเทศไทย เช่น 08x-xxx-xxxx, 08xxxxxxxx, 02-xxx-xxxx
        val phoneRegex = Regex("(0[689]\\d[\\s-]?\\d{3}[\\s-]?\\d{4})|(0\\d[\\s-]?\\d{3}[\\s-]?\\d{4})")
        val result = phoneRegex.find(text)

        return result?.value?.replace(Regex("[\\s-]"), "") ?: ""
    }
}