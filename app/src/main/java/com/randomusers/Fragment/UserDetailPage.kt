package com.randomusers.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.randomusers.Base.BaseFragment
import com.randomusers.R
import com.randomusers.databinding.FragmentUserDetailBinding
import com.randomusers.viewModel.UserDetailViewModel
import com.randomusers.viewModelFactory.UserDetailViewModelFactory


class UserDetailPage : BaseFragment(),View.OnClickListener {
    private lateinit var binding: FragmentUserDetailBinding
    private lateinit var detailViewModel: UserDetailViewModel
    var mobileNumber:String=""
    var emailId:String=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(layoutInflater)
        detailViewModel = ViewModelProvider(
            requireActivity(),
            UserDetailViewModelFactory(requireActivity())
        ).get(UserDetailViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this

        val userId = requireArguments().getInt("userId")
        detailViewModel.getRandomUserDetail(userId)

        detailViewModel.randomUserData.observe(requireActivity(), Observer {
            it?.let { randomUserData ->
                if (randomUserData != null && requireActivity() != null) {
                    val userImageUrl: String = randomUserData.picture!!.medium
                    if (userImageUrl!!.isNotEmpty()) {

                        val options = RequestOptions()
                        options.placeholder(R.drawable.user_thumb)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .error(R.drawable.user_thumb)
                        Glide.with(requireActivity())
                            .load(userImageUrl)
                            .transform(CenterCrop(), RoundedCorners(10))
                            .apply(options)
                            .into(binding.detailUserImage)
                    } else {
                        binding.detailUserImage.setImageResource(R.drawable.user_thumb)
                    }
                    mobileNumber = randomUserData.phone!!
                    emailId = randomUserData.email!!
                    detailViewModel.userAddress.value =
                        randomUserData.userLocation!!.street.number + ", " + randomUserData.userLocation!!.street.name + ", " + randomUserData.userLocation!!.city + ", " + randomUserData.userLocation!!.state
                }
            }
        })
        binding.toolbarBack.setOnClickListener(this)
        binding.mailButton.setOnClickListener(this)
        binding.callButton.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().viewModelStore.clear()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.toolbarBack -> {
                requireActivity().onBackPressed()
            }
            R.id.mailButton -> {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data =
                    Uri.parse("mailto:" + emailId)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.callButton -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:"+mobileNumber)
                startActivity(intent)
            }
        }
    }

}