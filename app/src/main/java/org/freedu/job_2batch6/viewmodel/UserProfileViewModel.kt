package org.freedu.job_2batch6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.freedu.job_2batch6.Model.UserProfile
import org.freedu.job_2batch6.Repository.UserProfileRepository
import org.freedu.job_2batch6.UserProfileDatabase.UserProfileDatabase

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserProfileRepository
    val allProfiles: LiveData<List<UserProfile>>
    val profileCount: LiveData<Int>

    init {
        val userProfileDao = UserProfileDatabase.getDatabase(application).userProfileDao()
        repository = UserProfileRepository(userProfileDao)
        allProfiles = repository.allProfiles
        profileCount = repository.profileCount
    }

    fun insertProfile(profile: UserProfile) = viewModelScope.launch {
        repository.insert(profile)
    }

    fun updateProfile(profile: UserProfile) = viewModelScope.launch {
        repository.update(profile)
    }

    fun deleteProfile(profile: UserProfile) = viewModelScope.launch {
        repository.delete(profile)
    }

    fun deleteProfileById(profileId: Int) = viewModelScope.launch {
        repository.deleteById(profileId)
    }

    fun getProfileById(profileId: Int): LiveData<UserProfile> {
        return repository.getProfileById(profileId)
    }
}