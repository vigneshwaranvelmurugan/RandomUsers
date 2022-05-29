package com.randomusers.listener

import androidx.fragment.app.Fragment
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.Utils.Fragments

interface ItemClickListener {
    fun loadMore()
    fun onItemClick(userData: RandomUserDbResult)
}