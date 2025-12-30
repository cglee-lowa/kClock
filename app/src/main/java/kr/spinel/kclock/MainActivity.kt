package kr.spinel.kclock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.spinel.kclock.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    //private lateinit var textViewClock: TextView

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //textViewClock = findViewById(R.id.textViewClock)
        handler.post(runnable) // 최초 실행
    }

    private fun koreanHourString(hour: Int): String {
        val hours = listOf("열두", "한", "두", "세", "네", "다섯", "여섯", "일곱", "여덟", "아홉", "열", "열한")
        return "${hours[hour % 12]}시"
    }

    private fun koreanMinuteString(minute: Int): String {
        if (minute !in 1..59) return ""
        if (minute == 30) return "반"
        val units = listOf("", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구")
        val tens = listOf("", "십", "이십", "삼십", "사십", "오십")

        val tenDigit = minute / 10
        val unitDigit = minute % 10

        return "${tens[tenDigit]}${units[unitDigit]}분"
    }

    private val runnable = object : Runnable {
        override fun run() {
            val now = LocalTime.now()

            val hour = now.hour     // 시 (0~23)
            val minute = now.minute // 분 (0~59)
            //val minute = now.second // 초 (0~59)

            binding.textClockHour.text = koreanHourString(hour)
            if (minute == 0) {
                binding.textClockMinute.visibility = TextView.GONE
            }
            else {
                binding.textClockMinute.visibility = TextView.VISIBLE
            }
            binding.textClockMinute.text = koreanMinuteString(minute)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 메모리 누수 방지를 위해 핸들러 제거
        handler.removeCallbacks(runnable)
    }
}