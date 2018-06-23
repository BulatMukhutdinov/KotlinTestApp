package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract

import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base.BaseView
import ru.bulat.mukhutdinov.kotlintestapp.presentation.util.RetryCallback

interface PhotoFeedView : BaseView<PhotoFeedPresenter>, RetryCallback {

    fun onSearch(query: String)

    fun onSearchClose()

    fun isVisible(): Boolean
}