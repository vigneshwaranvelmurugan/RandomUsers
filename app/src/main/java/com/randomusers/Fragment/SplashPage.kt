package com.randomusers.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.randomusers.Base.BaseFragment
import com.randomusers.Utils.CommonUtil
import com.randomusers.Utils.Fragments
import com.randomusers.databinding.FragmentSplashBinding
import com.randomusers.viewModel.SplashViewModel
import com.randomusers.viewModelFactory.SplashViewModelFactory

class SplashPage : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        splashViewModel = ViewModelProvider(
            this,
            SplashViewModelFactory()
        ).get(SplashViewModel::class.java)
        binding.viewModel = splashViewModel
        Handler(Looper.getMainLooper()).postDelayed({
            fragmentTransactionListener.transactFragment(
                HomePage(),
                Fragments.HOME_FRAGMENT,
                false
            )
        }, CommonUtil.splashDuration)
        return binding.root
    }

}