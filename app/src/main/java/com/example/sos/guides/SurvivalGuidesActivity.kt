package com.example.sos.guides

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sos.R
import com.example.sos.adapter.GuidesAdapter
import com.example.sos.databinding.ActivitySurvivalGuidesBinding
import com.example.sos.models.SurvivalGuide
import com.example.sos.viewmodel.GuidesViewModel

// SurvivalGuidesActivity.kt
class SurvivalGuidesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySurvivalGuidesBinding
    private lateinit var guidesViewModel: GuidesViewModel
    private lateinit var adapter: GuidesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurvivalGuidesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        guidesViewModel = ViewModelProvider(this).get(GuidesViewModel::class.java)

        // ตั้งค่า RecyclerView
        adapter = GuidesAdapter { guide ->
            showGuideDialog(guide)
        }
        binding.rvGuides.layoutManager = LinearLayoutManager(this)
        binding.rvGuides.adapter = adapter

        // โหลดข้อมูลคู่มือ
        guidesViewModel.getAllGuides().observe(this) { guides ->
            adapter.submitList(guides)
        }

        // ปุ่มย้อนกลับ
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showGuideDialog(guide: SurvivalGuide) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_guide)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvGuideTitle)
        val tvContent = dialog.findViewById<TextView>(R.id.tvGuideContent)
        val btnClose = dialog.findViewById<Button>(R.id.btnCloseGuide)

        tvTitle.text = guide.title
        tvContent.text = guide.content

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}