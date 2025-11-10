package org.freedu.job_2batch6.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val dateOfBirth: String,
    val district: String,
    val mobile: String,
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)