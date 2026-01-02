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

    fun getKoreanHour(h: Int): String =
        "${"열두,한,두,세,네,다섯,여섯,일곱,여덟,아홉,열,열한".split(",")[h % 12]}시"

    fun getKoreanMinute(m: Int): String = when (m) {
        0 -> ""
        30 -> "반"
        else -> (if (m < 20) "" else "  이삼사오"[m / 10].toString()) +
                (if (m >= 10) "십" else "") +
                " 일이삼사오육칠팔구"[m % 10]
    }.trim() + if (m > 0 && m != 30) "분" else ""

    private val runnable = object : Runnable {
        override fun run() {
            val now = LocalTime.now()

            val hour = now.hour     // 시 (0~23)
            val minute = now.minute // 분 (0~59)
            //val minute = now.second // 초 (0~59)

            binding.textClockHour.text = getKoreanHour(hour)
            if (minute == 0) {
                binding.textClockMinute.visibility = TextView.GONE
            }
            else {
                binding.textClockMinute.visibility = TextView.VISIBLE
            }
            binding.textClockMinute.text = getKoreanMinute(minute)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 메모리 누수 방지를 위해 핸들러 제거
        handler.removeCallbacks(runnable)
    }
}