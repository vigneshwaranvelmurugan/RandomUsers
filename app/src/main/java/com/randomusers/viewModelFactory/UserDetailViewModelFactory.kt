package com.randomusers.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.randomusers.viewModel.UserDetailViewModel

class UserDetailViewModelFactory(context: Context) : ViewModelProvider.Factory {
    var mContext=context
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(mContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}