package com.randomusers.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.randomusers.Adapter.RandomUserListAdapter
import com.randomusers.Base.BaseFragment
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.R
import com.randomusers.Utils.ApiStatus
import com.randomusers.Utils.CommonUtil
import com.randomusers.Utils.Fragments
import com.randomusers.databinding.FragmentHomeBinding
import com.randomusers.listener.ItemClickListener
import com.randomusers.viewModel.HomeViewModel
import com.randomusers.viewModelFactory.HomeViewModelFactory


class HomePage : BaseFragment(), ItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listRandomUsers: ArrayList<RandomUserDbResult?>
    var randomUserAdapter: RandomUserListAdapter? = null
    var pageNo: Int = 1
    var currentLocationRequestCode: Int = 120
    var locationRequestCode: Int = 121

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeViewModel = ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory(requireActivity())
        ).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
        binding.homeSearchEdit.setUpEditText()
        setupRecyclerView()
        loaderEnable(true)
        homeViewModel.getRandomData(pageNo.toString())
        homeViewModel.randomUserList.observe(requireActivity(), Observer {
            it?.let { apiResponse ->
                when (apiResponse.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        apiResponse.data?.let {
                            loaderEnable(false)
                            pageNo++
                            if (listRandomUsers.isNotEmpty() && listRandomUsers.get(listRandomUsers.size - 1) == null) {
                                listRandomUsers.removeAt(listRandomUsers.size - 1)
                            }
                            listRandomUsers.addAll(it)
                            randomUserAdapter!!.updateList(listRandomUsers)
                            randomUserAdapter!!.setLoaded()
                        }
                    }
                    ApiStatus.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.please_try_again),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    ApiStatus.LOADING -> {

                    }
                }
            }
        })
        homeViewModel.randomUserSearchList.observe(requireActivity(), Observer {
            it?.let { searchResult ->
                if (searchResult.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.no_data_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                randomUserAdapter!!.updateList(searchResult)
            }
        })
        homeViewModel.weatherdetail.observe(requireActivity(), Observer {
            it?.let { apiResponse ->
                when (apiResponse.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        homeViewModel.weatherLoaderEnable.value=View.GONE
                        homeViewModel.weatherDetailEnable.value=View.VISIBLE
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
                            homeViewModel.weatherMessage.value=weatherDetail.cityName+", "+weatherDetail.weather.description
                            homeViewModel.weatherDegree.value=weatherDetail.temp
                        }
                    }
                    ApiStatus.ERROR -> {

                    }
                    ApiStatus.LOADING -> {
                        homeViewModel.weatherLoaderEnable.value=View.VISIBLE
                        homeViewModel.weatherDetailEnable.value=View.GONE
                    }
                }

            }
        })
        if(checkLocationPermission()){
            createGpsLocationRequest()
        }
        return binding.root
    }

    fun setupRecyclerView() {
        listRandomUsers = ArrayList()
        binding.homeList.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.homeList.layoutManager = layoutManager
        randomUserAdapter =
            RandomUserListAdapter(requireActivity(), listRandomUsers, binding.homeList, this)

        binding.homeList.adapter = randomUserAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.setUpEditText() {

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val searchIcon = R.drawable.ic_baseline_search
                val clearIcon =
                    if (editable?.isNotEmpty() == true) R.drawable.ic_baseline_clear_24 else 0
                setCompoundDrawablesWithIntrinsicBounds(searchIcon, 0, clearIcon, 0)
                homeViewModel.filterRandomUser(editable.toString())
                if (editable!!.isEmpty()) {
                    randomUserAdapter!!.setLoaded()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    override fun loadMore() {
        if (binding.homeSearchEdit.text.isEmpty()){
            listRandomUsers.add(null)
            randomUserAdapter!!.notifyItemInserted(listRandomUsers.size - 1)
            homeViewModel.getRandomData(pageNo.toString())
        }
    }

    override fun onItemClick(userData: RandomUserDbResult) {
        val userDetailPage=UserDetailPage()
        val args = Bundle()
        args.putInt("userId", userData.random_id!!)
        userDetailPage.arguments=args
        fragmentTransactionListener.transactFragment(
            userDetailPage,
            Fragments.HOME_FRAGMENT,
            true
        )
    }

    fun loaderEnable(enable: Boolean) {
        if (enable) {
            binding.progressBarLayout.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
        } else {
            binding.progressBarLayout.visibility = View.GONE
            binding.mainLayout.visibility = View.VISIBLE
        }
    }

    fun createGpsLocationRequest() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest).setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            getCurrentLocation()
        }

        task.addOnFailureListener(requireActivity(), OnFailureListener { e ->
            if (e is ResolvableApiException) {
                val resolvableApiException = e as ResolvableApiException
                resolvableApiException.startResolutionForResult(
                    requireActivity(),
                    currentLocationRequestCode
                )
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==currentLocationRequestCode&&resultCode==Activity.RESULT_OK){
            Handler(Looper.getMainLooper()).postDelayed({
                getCurrentLocation()
            }, 10000)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                createGpsLocationRequest()
            }
        }
    }

    fun checkLocationPermission():Boolean{
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationRequestCode
            )
            return false
        }
    }

    fun getCurrentLocation(){
        try {
            var latitude:Double=0.00
            var longitude:Double=0.00

            val permission = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permission == PackageManager.PERMISSION_GRANTED){
                val locationManager =
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
                val gps_loc = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (gps_loc != null) {
                    latitude = gps_loc.latitude
                    longitude = gps_loc.longitude
                } else if (network_loc != null) {
                    latitude = network_loc.latitude
                    longitude = network_loc.longitude
                }
                if(latitude!=0.0&&longitude!=0.0){
                   homeViewModel.weatherViewEnable.value=View.VISIBLE
                   homeViewModel.getWeatherDetail(longitude.toString(),latitude.toString(),requireActivity())
               }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}