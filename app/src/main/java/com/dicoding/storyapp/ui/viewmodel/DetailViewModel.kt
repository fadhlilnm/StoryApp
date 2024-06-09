package com.dicoding.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.DetailRepository
import com.dicoding.storyapp.data.response.DetailStoryResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val detailRepository: DetailRepository) : ViewModel() {
    val detailStory: LiveData<DetailStoryResponse> = detailRepository.detailStory
    val errorMessage: LiveData<String> = detailRepository.errorMessage

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            detailRepository.getDetailStory(id)
        }
    }
}
