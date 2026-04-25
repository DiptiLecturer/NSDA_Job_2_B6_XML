package org.freedu.job_2batch6

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.freedu.job_2batch6.Adapter.ProfileAdapter
import org.freedu.job_2batch6.databinding.ActivityProfileListBinding
import org.freedu.job_2batch6.viewmodel.UserProfileViewModel

class ActivityProfileList : AppCompatActivity() {

    private lateinit var binding: ActivityProfileListBinding
    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var adapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        // FAB → go to Add Profile
        binding.fabAddProfile.setOnClickListener {
            startActivity(Intent(this, ActivityAddProfile::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = ProfileAdapter(
            onEditClick = { profile ->
                // Pass profile ID to AddProfile for editing
                val intent = Intent(this, ActivityAddProfile::class.java)
                intent.putExtra("PROFILE_ID", profile.id)
                startActivity(intent)
            },
            onDeleteClick = { profile ->
                viewModel.deleteProfile(profile)
            },
            onItemClick = { profile ->
                // Open single profile detail view
                val intent = Intent(this, ActivitySingleProfile::class.java)
                intent.putExtra("PROFILE_ID", profile.id)
                startActivity(intent)
            }
        )

        binding.recyclerViewProfiles.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProfiles.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        // Observe profile list
        viewModel.allProfiles.observe(this) { profiles ->
            adapter.submitList(profiles)

            // Show/hide empty state
            if (profiles.isEmpty()) {
                binding.recyclerViewProfiles.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.recyclerViewProfiles.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            }
        }

        // Observe profile count
        viewModel.profileCount.observe(this) { count ->
            binding.tvProfileCount.text = "Total Profiles: $count"
        }
    }
}