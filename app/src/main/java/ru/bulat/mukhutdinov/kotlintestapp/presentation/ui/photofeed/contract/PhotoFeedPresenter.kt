package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.NetworkStatusEntity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainRouter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base.BasePresenter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.util.OnPhotoClickListener

interface PhotoFeedPresenter : BasePresenter<PhotoFeedView, MainRouter>, OnPhotoClickListener {

    var entities: LiveData<PagedList<PhotoBindable>>?

    fun loadAll()

    fun search(query: String)

    fun retry()

    fun refresh()

    fun getNetworkState(): LiveData<NetworkStatusEntity>

    fun getPhotosState(): LiveData<NetworkStatusEntity>
}