package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui

import android.widget.ImageView
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base.BaseRouter

interface MainRouter : BaseRouter {

    fun showPhotoFeed()

    fun showPhoto(photo: PhotoBindable, image: ImageView)
}