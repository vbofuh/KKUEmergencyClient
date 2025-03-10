package com.example.sos.chat
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var adapter: MessageAdapter
    private var chatId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatId = intent.getStringExtra("chatId") ?: ""

        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // ตั้งค่า RecyclerView
        adapter = MessageAdapter()
        binding.rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // แสดงข้อความล่าสุดด้านล่าง
        }
        binding.rvMessages.adapter = adapter

        // โหลดข้อมูลแชท
        chatViewModel.getMessages(chatId).observe(this) { messages ->
            adapter.submitList(messages)
            if (messages.isNotEmpty()) {
                binding.rvMessages.scrollToPosition(messages.size - 1)
            }
        }

        // ตั้งค่าปุ่มส่งข้อความ
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotEmpty()) {
                chatViewModel.sendMessage(chatId, message)
                binding.etMessage.text.clear()
            }
        }

        // ปุ่มย้อนกลับ
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}