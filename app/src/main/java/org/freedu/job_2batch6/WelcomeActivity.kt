package org.freedu.job_2batch6

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.freedu.job_2batch6.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnProfileList.setOnClickListener {
            startActivity(Intent(this, ActivityProfileList::class.java))
        }
    }
}