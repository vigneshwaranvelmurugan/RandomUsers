package com.randomusers.viewModel

import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.randomusers.R

class SplashViewModel : ViewModel() {
    var isAnimateImage: Boolean? = true

    companion object {
        @BindingAdapter("shakeAnimation")
        @JvmStatic
        fun shakeAnimation(view: View, boolean:Boolean) {
            if (boolean) {
                val viewAnim = AnimationUtils.loadAnimation(view.context, R.anim.shake_animation)
                view.startAnimation(viewAnim)
            }
        }
    }
}