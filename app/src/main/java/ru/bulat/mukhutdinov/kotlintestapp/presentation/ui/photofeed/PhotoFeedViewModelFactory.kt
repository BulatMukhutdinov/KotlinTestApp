package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed

import androidx.lifecycle.ViewModel
import ru.bulat.mukhutdinov.kotlintestapp.domain.PhotoRepository
import ru.bulat.mukhutdinov.kotlintestapp.presentation.factory.BaseViewModelFactory
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainRouter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedView

class PhotoFeedViewModelFactory(private val view: PhotoFeedView,
                                private val router: MainRouter,
                                private val photoRepository: PhotoRepository) : BaseViewModelFactory() {

    override fun viewModel(): ViewModel =
            PhotoFeedViewModel(photoRepository, view, router)
}