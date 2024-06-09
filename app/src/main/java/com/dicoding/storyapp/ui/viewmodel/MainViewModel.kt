package com.dicoding.storyapp.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.local.StoryEntity
import com.dicoding.storyapp.data.preference.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<PagingData<StoryEntity>>()
    val stories: LiveData<PagingData<StoryEntity>> = _stories

    fun fetchStories(token: String) {
        viewModelScope.launch {
            userRepository.getStories(token).observeForever { pagingData ->
                _stories.value = pagingData
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
