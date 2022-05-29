package com.randomusers.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.randomusers.Base.BaseFragment
import com.randomusers.R
import com.randomusers.Utils.ApiStatus
import com.randomusers.Utils.CommonUtil
import com.randomusers.databinding.FragmentUserDetailBinding
import com.randomusers.viewModel.UserDetailViewModel
import com.randomusers.viewModelFactory.UserDetailViewModelFactory


class UserDetailPage : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentUserDetailBinding
    private lateinit var detailViewModel: UserDetailViewModel
    var mobileNumber: String = ""
    var emailId: String = ""

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
                    detailViewModel.getWeatherDetail(
                        randomUserData.userLocation!!.coordinates.longitude,
                        randomUserData.userLocation!!.coordinates.latitude,
                        requireContext()
                    )
                }
            }
        })
        detailViewModel.weatherdetail.observe(requireActivity(), Observer {
            it?.let { apiResponse ->
                when (apiResponse.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        detailViewModel.weatherLoaderEnable.value=View.GONE
                        detailViewModel.weatherDetailEnable.value=View.VISIBLE
                        apiResponse.data?.let {
                            val weatherDetail=apiResponse.data[0]
                            val weatherUrl: String = CommonUtil.weatherImageUrl+weatherDetail.weather.icon+".png"
                            if (weatherUrl!!.isNotEmpty()) {
                                val options = RequestOptions()
                                options.placeholder(R.drawable.ic_baseline_wb_cloudy_24)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .error(R.drawable.ic_baseline_wb_cloudy_24)
                                Glide.with(requireActivity())
                                    .load(weatherUrl)
                                    .apply(options)
                                    .into(binding.weatherLogo)
                            } else {
                                binding.weatherLogo.setImageResource(R.drawable.ic_baseline_wb_cloudy_24)
                            }
                            detailViewModel.weatherMessage.value=weatherDetail.weather.description
                            detailViewModel.weatherDegree.value=weatherDetail.temp
                        }
                    }
                    ApiStatus.ERROR -> {

                    }
                    ApiStatus.LOADING -> {
                        detailViewModel.weatherLoaderEnable.value=View.VISIBLE
                        detailViewModel.weatherDetailEnable.value=View.GONE
                    }
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
        if(detailViewModel.callWeatherApi!=null){
            detailViewModel.callWeatherApi!!.cancel()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
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
                intent.data = Uri.parse("tel:" + mobileNumber)
                startActivity(intent)
            }
        }
    }


}