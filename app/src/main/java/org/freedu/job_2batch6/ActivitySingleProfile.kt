package org.freedu.job_2batch6

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.freedu.job_2batch6.databinding.ActivitySingleProfileBinding
import org.freedu.job_2batch6.viewmodel.UserProfileViewModel

class ActivitySingleProfile : AppCompatActivity() {
    private lateinit var binding: ActivitySingleProfileBinding
    private lateinit var viewModel: UserProfileViewModel
    private var profileId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySingleProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]

        profileId = intent.getIntExtra("PROFILE_ID", 0)

        loadProfileData()

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, ActivityAddProfile::class.java)
            intent.putExtra("PROFILE_ID", profileId)
            intent.putExtra("IS_EDIT", true)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun loadProfileData() {
        viewModel.getProfileById(profileId).observe(this) { profile ->
            profile?.let {
                binding.tvName.text = it.name
                binding.tvEmail.text = it.email
                binding.tvDob.text = it.dateOfBirth
                binding.tvDistrict.text = it.district
                binding.tvMobile.text = it.mobile
                binding.tvAddress.text = if (it.address.isEmpty()) "N/A" else it.address
            }
        }
    }

    private fun showDeleteConfirmation() {
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