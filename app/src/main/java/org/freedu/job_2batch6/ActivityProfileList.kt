package org.freedu.job_2batch6

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.freedu.job_2batch6.Adapter.ProfileAdapter
import org.freedu.job_2batch6.databinding.ActivityProfileListBinding
import org.freedu.job_2batch6.viewmodel.UserProfileViewModel

class ProfileListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileListBinding
    private lateinit var viewModel: UserProfileViewModel
    private lateinit var adapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]

        setupRecyclerView()
        observeData()

        binding.fabAddProfile.setOnClickListener {
            startActivity(Intent(this, ActivityAddProfile::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = ProfileAdapter(
            onEditClick = { profile ->
                val intent = Intent(this, ActivityAddProfile::class.java)
                intent.putExtra("PROFILE_ID", profile.id)
                intent.putExtra("IS_EDIT", true)
                startActivity(intent)
            },
            onDeleteClick = { profile ->
                showDeleteConfirmation(profile.id, profile.name)
            },
            onItemClick = { profile ->
                val intent = Intent(this, ActivitySingleProfile::class.java)
                intent.putExtra("PROFILE_ID", profile.id)
                startActivity(intent)
            }
        )

        binding.recyclerViewProfiles.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProfiles.adapter = adapter
    }

    private fun observeData() {
        viewModel.allProfiles.observe(this) { profiles ->
            profiles?.let {
                adapter.submitList(it)
                binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                binding.recyclerViewProfiles.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            }
        }

        viewModel.profileCount.observe(this) { count ->
            binding.tvProfileCount.text = "Total Profiles: ${count ?: 0}"
        }
    }

    private fun showDeleteConfirmation(profileId: Int, profileName: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Profile")
            .setMessage("Are you sure you want to delete $profileName's profile?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteProfileById(profileId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}