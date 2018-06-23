package ru.bulat.mukhutdinov.kotlintestapp.presentation.util

import android.widget.ImageView
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable

interface OnPhotoClickListener {

    fun onClick(photo: PhotoBindable, image: ImageView)
}