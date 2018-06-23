package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.network_state.*
import kotlinx.android.synthetic.main.photo_feed.*
import org.kodein.di.generic.instance
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.data.dto.NetworkStatusDto
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.NetworkStatusEntity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainActivity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainRouter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base.BaseFragment
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.adapter.PhotoAdapter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedPresenter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedView


class PhotoFeedFragment : BaseFragment<PhotoFeedPresenter, PhotoFeedView, MainRouter>(), PhotoFeedView {

    companion object {
        fun newInstance() = PhotoFeedFragment()
    }

    override val presenter: PhotoFeedPresenter by instance()

    private lateinit var adapter: PhotoAdapter

    private var isReloadOnSearchCloseNeeded: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.photo_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        adapter = PhotoAdapter(this, presenter)
        photos.adapter = adapter
        subscribeToPhotosUpdate()

        retry.setOnClickListener { _ -> retry() }
    }

    private fun subscribeToPhotosUpdate() {
        presenter.entities?.observe(this, Observer { photos -> adapter.submitList(photos) })

        presenter.getNetworkState().observe(this, Observer { state -> adapter.setNetworkState(state) })

        presenter.getPhotosState().observe(this, Observer { networkState ->
            if (networkState != null) {
                if (adapter.currentList != null && adapter.currentList!!.size > 0) {
                    refresh.isRefreshing = networkState.status == NetworkStatusEntity.LOADING.status
                } else {
                    setInitialLoadingState(networkState)
                }
            }
        })

        refresh.setOnRefreshListener { presenter.refresh() }
    }

    private fun setInitialLoadingState(networkState: NetworkStatusEntity) {
        error.visibility = if (networkState.message != null) View.VISIBLE else View.GONE

        if (networkState.message != null) {
            error.text = networkState.message
        }

        retry.visibility = if (networkState.status == NetworkStatusDto.FAILED) View.VISIBLE else View.GONE
        loading.visibility = if (networkState.status == NetworkStatusDto.RUNNING) View.VISIBLE else View.GONE

        refresh.isEnabled = networkState.status == NetworkStatusDto.SUCCESS

        if (networkState.status == NetworkStatusDto.FAILED) {
            adapter.setNetworkState(NetworkStatusEntity.LOADED)
        }
    }

    override fun onSearch(query: String) {
        presenter.search(query)
        subscribeToPhotosUpdate()
        isReloadOnSearchCloseNeeded = true
    }

    override fun onSearchClose() {
        if (isReloadOnSearchCloseNeeded) {
            presenter.loadAll()
            subscribeToPhotosUpdate()
        }
        isReloadOnSearchCloseNeeded = false
    }

    override fun retry() {
        presenter.retry()
    }
}