package com.example.sos.guides

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