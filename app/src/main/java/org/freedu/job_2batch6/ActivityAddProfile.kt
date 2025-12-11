package org.freedu.job_2batch6

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.freedu.job_2batch6.Model.UserProfile
import org.freedu.job_2batch6.databinding.ActivityAddProfileBinding
import org.freedu.job_2batch6.viewmodel.UserProfileViewModel
import java.util.Calendar

class ActivityAddProfile : AppCompatActivity() {

    private lateinit var binding: ActivityAddProfileBinding
    private lateinit var viewModel: UserProfileViewModel
    private var isEditMode = false
    private var profileId = 0
    private var currentProfile: UserProfile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        isEditMode = intent.getBooleanExtra("IS_EDIT", false)
        profileId = intent.getIntExtra("PROFILE_ID", 0)

        setupUI()
        setupDistrictSpinner()

        if (isEditMode) {
            loadProfileData()
        }

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }

        binding.etDateOfBirth.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupUI() {
        title = if (isEditMode) "Edit Profile" else "Add Profile"
        binding.btnSaveProfile.text = if (isEditMode) "Update Profile" else "Save Profile"
    }

    private fun setupDistrictSpinner() {
        val districts = arrayOf(
            "Select District", "Dhaka", "Chittagong", "Rajshahi", "Khulna",
            "Barisal", "Sylhet", "Rangpur", "Mymensingh"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDistrict.adapter = adapter
    }

    private fun loadProfileData() {
        viewModel.getProfileById(profileId).observe(this) { profile ->
            profile?.let {
                currentProfile = it
                binding.etName.setText(it.name)
                binding.etEmail.setText(it.email)
                binding.etDateOfBirth.setText(it.dateOfBirth)
                binding.etMobile.setText(it.mobile)
                binding.etAddress.setText(it.address)

                val districtPosition = (binding.spinnerDistrict.adapter as ArrayAdapter<String>)
                    .getPosition(it.district)
                if (districtPosition >= 0) {
                    binding.spinnerDistrict.setSelection(districtPosition)
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.etDateOfBirth.setText(formattedDate)
        }, year, month, day).show()
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val dob = binding.etDateOfBirth.text.toString().trim()
        val district = binding.spinnerDistrict.selectedItem.toString()
        val mobile = binding.etMobile.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        if (!validateInputs(name, email, dob, district, mobile)) {
            return
        }

        val profile = UserProfile(
            id = if (isEditMode) profileId else 0,
            name = name,
            email = email,
            dateOfBirth = dob,
            district = district,
            mobile = mobile,
            address = address,
            createdAt = currentProfile?.createdAt ?: System.currentTimeMillis()
        )

        if (isEditMode) {
            viewModel.updateProfile(profile)
            Snackbar.make(
                findViewById(android.R.id.content),
                "Profile updated successfully",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            viewModel.insertProfile(profile)
            Snackbar.make(
                findViewById(android.R.id.content),
                "Profile saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
        }

        finish()
    }

    private fun validateInputs(
        name: String, email: String, dob: String,
        district: String, mobile: String
    ): Boolean {
        when {
            name.isEmpty() -> {
                binding.etName.error = "Name is required"
                return false
            }
            email.isEmpty() -> {
                binding.etEmail.error = "Email is required"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.etEmail.error = "Invalid email format"
                return false
            }
            dob.isEmpty() -> {
                binding.etDateOfBirth.error = "Date of birth is required"
                return false
            }
            district == "Select District" -> {
                Toast.makeText(this, "Please select a district", Toast.LENGTH_SHORT).show()
                return false
            }
            mobile.isEmpty() -> {
                binding.etMobile.error = "Mobile number is required"
                return false
            }
            mobile.length < 11 -> {
                binding.etMobile.error = "Invalid mobile number"
                return false
            }
        }
        return true
    }
}