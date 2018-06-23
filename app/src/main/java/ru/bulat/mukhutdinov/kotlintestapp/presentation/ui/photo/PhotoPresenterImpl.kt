package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo

import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainRouter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.contract.PhotoPresenter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.contract.PhotoView

class PhotoPresenterImpl(override var view: PhotoView?, override var router: MainRouter?) : PhotoPresenter