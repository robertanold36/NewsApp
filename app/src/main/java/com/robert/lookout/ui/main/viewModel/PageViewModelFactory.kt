package com.robert.lookout.ui.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robert.lookout.ui.main.repository.ApiRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")

class PageViewModelFactory @Inject constructor(private val repository: ApiRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return PageViewModel(repository) as T
    }
}