package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.adapter

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.NetworkStatusEntity
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.NetworkStatusEntity.Companion.LOADED
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import ru.bulat.mukhutdinov.kotlintestapp.presentation.util.OnPhotoClickListener
import ru.bulat.mukhutdinov.kotlintestapp.presentation.util.RetryCallback


class PhotoAdapter(private val retryCallback: RetryCallback, private val clickListener: OnPhotoClickListener)
    : PagedListAdapter<PhotoBindable, RecyclerView.ViewHolder>(PhotoDiffCallback) {

    private var networkState: NetworkStatusEntity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                R.layout.photo_item -> PhotoViewHolder.create(parent, clickListener)
                R.layout.network_state -> NetworkStatusViewHolder.create(parent, retryCallback)
                else -> throw IllegalArgumentException("unknown view type")
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.photo_item -> {
                getItem(position)?.let {
                    (holder as PhotoViewHolder).bindTo(it)
                    ViewCompat.setTransitionName(holder.image, it.url)
                }
            }

            R.layout.network_state -> networkState?.let { (holder as NetworkStatusViewHolder).bindTo(it) }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state
        } else {
            R.layout.photo_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkStatusEntity?) {
        if (currentList != null) {
            if (itemCount != 0) {
                val previousState = networkState
                val hadExtraRow = hasExtraRow()
                networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        private val PhotoDiffCallback = object : DiffUtil.ItemCallback<PhotoBindable>() {
            override fun areItemsTheSame(oldItem: PhotoBindable, newItem: PhotoBindable): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: PhotoBindable, newItem: PhotoBindable): Boolean {
                return oldItem.url == newItem.url
            }
        }
    }
}

