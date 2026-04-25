package org.freedu.job_2batch6

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.freedu.job_2batch6.Model.UserProfile
import org.freedu.job_2batch6.databinding.ActivityAddProfileBinding
import org.freedu.job_2batch6.viewmodel.UserProfileViewModel
import java.util.Calendar

class ActivityAddProfile : AppCompatActivity() {

    private lateinit var binding: ActivityAddProfileBinding
    private val viewModel: UserProfileViewModel by viewModels()

    // Holds the profile being edited (null = add mode)
    private var existingProfile: UserProfile? = null

    private val districts = listOf(
        "Dhaka", "Chittagong", "Sylhet", "Rajshahi",
        "Khulna", "Barisal", "Rangpur", "Mymensingh"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDistrictSpinner()
        setupDatePicker()

        // Check if we are in Edit mode
        val profileId = intent.getIntExtra("PROFILE_ID", -1)
        if (profileId != -1) {
            loadProfileForEditing(profileId)
        }

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun setupDistrictSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDistrict.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun setupDatePicker() {
        binding.etDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    binding.etDateOfBirth.setText("$day/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun loadProfileForEditing(profileId: Int) {
        viewModel.getProfileById(profileId).observe(this) { profile ->
            profile?.let {
                existingProfile = it

                // Populate all fields
                binding.etName.setText(it.name)
                binding.etEmail.setText(it.email)
                binding.etDateOfBirth.setText(it.dateOfBirth)
                binding.etMobile.setText(it.mobile)
                binding.etAddress.setText(it.address)

                // Set spinner to saved district
                val districtIndex = districts.indexOf(it.district)
                if (districtIndex >= 0) {
                    binding.spinnerDistrict.setSelection(districtIndex)
                }

                // Update button label
                binding.btnSaveProfile.text = "Update Profile"
                binding.tvAddProfile.text = "Edit Profile"
            }
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val dob = binding.etDateOfBirth.text.toString().trim()
        val district = binding.spinnerDistrict.selectedItem.toString()
        val mobile = binding.etMobile.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        // Validation
        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            return
        } else binding.tilName.error = null

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            return
        } else binding.tilEmail.error = null

        if (dob.isEmpty()) {
            binding.tilDateOfBirth.error = "Date of birth is required"
            return
        } else binding.tilDateOfBirth.error = null

        if (mobile.isEmpty()) {
            binding.tilMobile.error = "Mobile number is required"
            return
        } else binding.tilMobile.error = null

        if (existingProfile != null) {
            // UPDATE — preserve original ID and createdAt
            val updated = existingProfile!!.copy(
                name = name,
                email = email,
                dateOfBirth = dob,
                district = district,
                mobile = mobile,
                address = address
            )
            viewModel.updateProfile(updated)
            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()

            // Go to ProfileList and clear back stack
            val intent = Intent(this, ActivityProfileList::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()

        } else {
            // INSERT
            val newProfile = UserProfile(
                name = name,
                email = email,
                dateOfBirth = dob,
                district = district,
                mobile = mobile,
                address = address
            )
            viewModel.insertProfile(newProfile)
            Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}