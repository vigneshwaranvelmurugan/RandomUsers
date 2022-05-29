package com.randomusers.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.randomusers.Adapter.RandomUserListAdapter
import com.randomusers.Base.BaseFragment
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.R
import com.randomusers.Utils.ApiStatus
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
}