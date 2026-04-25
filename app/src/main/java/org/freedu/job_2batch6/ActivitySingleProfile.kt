package org.freedu.job_2batch6

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import org.freedu.job_2batch6.databinding.ActivitySingleProfileBinding
import org.freedu.job_2batch6.viewmodel.UserProfileViewModel

class ActivitySingleProfile : AppCompatActivity() {

    private lateinit var binding: ActivitySingleProfileBinding
    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profileId = intent.getIntExtra("PROFILE_ID", -1)
        if (profileId == -1) {
            finish()
            return
        }

        // Observe and display the profile
        viewModel.getProfileById(profileId).observe(this) { profile ->
            profile?.let {
                binding.tvName.text     = it.name
                binding.tvEmail.text    = it.email
                binding.tvDob.text      = it.dateOfBirth
                binding.tvDistrict.text = it.district
                binding.tvMobile.text   = it.mobile
                binding.tvAddress.text  = it.address.ifEmpty { "N/A" }
            }
        }

        // Edit button → go to AddProfile in edit mode
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, ActivityAddProfile::class.java)
            intent.putExtra("PROFILE_ID", profileId)
            startActivity(intent)
        }

        // Delete button → confirm then delete
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete this profile?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteProfileById(profileId)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}