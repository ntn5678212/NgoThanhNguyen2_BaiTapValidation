
package com.example.ngothanhnguyen_bttailop2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ngothanhnguyen_bttailop2.databinding.ActivityStudentDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvName.text =
            intent.getStringExtra("NAME") ?: "Chưa có"

        binding.tvEmail.text =
            intent.getStringExtra("EMAIL") ?: "Chưa có"

        binding.tvPhone.text =
            intent.getStringExtra("PHONE") ?: "Chưa có"

        binding.tvStudentId.text =
            intent.getStringExtra("STUDENT_ID") ?: "Chưa có"

        binding.tvClass.text =
            intent.getStringExtra("CLASS") ?: "Chưa có"

        binding.btnBack.setOnClickListener {
            val backIntent = Intent(
                this,
                MainActivity::class.java
            ).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
            }

            startActivity(backIntent)
            finish()
        }
    }
}
