package com.randomusers.Base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.randomusers.listener.FragmentTransactionListener

open class BaseFragment : Fragment() {
    lateinit var fragmentTransactionListener: FragmentTransactionListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            fragmentTransactionListener = (it as? FragmentTransactionListener)!!
        }
    }
}