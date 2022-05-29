package com.randomusers.listener

import androidx.fragment.app.Fragment
import com.randomusers.Utils.Fragments

interface FragmentTransactionListener {
    fun transactFragment(fragment: Fragment, fragmentName: Fragments, isAddToBackStack: Boolean)
}