package com.randomusers.Activity

import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.randomusers.Fragment.SplashPage
import com.randomusers.R
import com.randomusers.Utils.Fragments
import com.randomusers.databinding.ActivityMainBinding
import com.randomusers.listener.FragmentTransactionListener


class MainActivity : AppCompatActivity(), FragmentTransactionListener {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        transactFragment(SplashPage(), Fragments.SPLASH_FRAGMENT, false)
    }

    override fun transactFragment(
        fragment: Fragment,
        fragmentName: Fragments,
        isAddToBackStack: Boolean
    ) {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.mainFrameLayout, fragment, fragmentName.toString())
        transaction.setReorderingAllowed(true)
        if (isAddToBackStack){
            transaction.addToBackStack(fragmentName.toString())
        }
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.mainFrameLayout)
        fragment!!.onActivityResult(requestCode, resultCode,data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragment = supportFragmentManager.findFragmentById(R.id.mainFrameLayout)
        fragment!!.onRequestPermissionsResult(requestCode, permissions,grantResults)
    }
}