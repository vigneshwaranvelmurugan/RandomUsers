package com.randomusers.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.R
import com.randomusers.databinding.ItemRandomUserBinding
import com.randomusers.listener.ItemClickListener

class RandomUserListAdapter(var context: Context, var listRandomUsers: List<RandomUserDbResult?>, val recyclerView: RecyclerView, val userListener: ItemClickListener) :
    RecyclerView.Adapter<RandomUserListAdapter.ViewHolder>() {
    val VIEW_ITEM = 1
    val VIEW_PROG = 0
    val visibleThreshold = 5
    var lastVisibleItem = 0
    var totalItemCount: Int = 0
    var loading = false

    init {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager: LinearLayoutManager? =
                recyclerView.layoutManager as LinearLayoutManager?
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    println("loadingValuesss "+loading)

                    totalItemCount = linearLayoutManager!!.getItemCount()
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                    if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        userListener.loadMore()
                        loading = true
                    }
                }
            })

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_ITEM) {
            return RandomUserViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_random_user, parent, false)
            )
        }
        return LoaderViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_loader, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is RandomUserViewHolder) {
            (holder as RandomUserViewHolder).bind(position)
        }
    }

    private inner class RandomUserViewHolder(itemView: View) : ViewHolder(itemView) {
        val binding = ItemRandomUserBinding.bind(itemView)
        fun bind(position: Int){
            val randomUserData=listRandomUsers[position]

            val userImageUrl: String? = randomUserData!!.picture!!.medium
            if (userImageUrl!!.isNotEmpty()) {

                val options = RequestOptions()
                options.placeholder(R.drawable.user_thumb)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.user_thumb)
                Glide.with(context)
                    .load(userImageUrl)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .apply(options)
                    .into(binding.itemUserImage)
            } else {
                binding.itemUserImage.setImageResource(R.drawable.user_thumb)
            }
            binding.itemUserName.text = randomUserData!!.name_first_last
            binding.root.setOnClickListener {
                userListener.onItemClick(randomUserData)
            }
        }
    }

    private inner class LoaderViewHolder(itemView: View) : ViewHolder(itemView) {
    }

    override fun getItemCount(): Int {
        return listRandomUsers.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listRandomUsers[position] != null) VIEW_ITEM else VIEW_PROG
    }

    fun updateList(mDataList1: List<RandomUserDbResult?>) {
        listRandomUsers = mDataList1
        notifyDataSetChanged()
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    fun setLoaded() {
        loading = false
    }
}

