package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.data.dto.NetworkStatusDto
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.NetworkStatusEntity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.util.RetryCallback


class NetworkStatusViewHolder(itemView: View, retryCallback: RetryCallback) : RecyclerView.ViewHolder(itemView) {
    private val retry: Button = itemView.findViewById(R.id.retry)
    private val error: TextView = itemView.findViewById(R.id.error)
    private val loading: ProgressBar = itemView.findViewById(R.id.loading)

    init {
        retry.setOnClickListener { _ -> retryCallback.retry() }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: RetryCallback): NetworkStatusViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.network_state, parent, false)
            return NetworkStatusViewHolder(view, retryCallback)
        }
    }

    fun bindTo(networkStatus: NetworkStatusEntity) {
        error.visibility = if (networkStatus.message != null) View.VISIBLE else View.GONE
        if (networkStatus.message != null) {
            error.text = networkStatus.message
        }

        retry.visibility = if (networkStatus.status === NetworkStatusDto.FAILED) View.VISIBLE else View.GONE
        loading.visibility = if (networkStatus.status === NetworkStatusDto.RUNNING) View.VISIBLE else View.GONE
    }
}