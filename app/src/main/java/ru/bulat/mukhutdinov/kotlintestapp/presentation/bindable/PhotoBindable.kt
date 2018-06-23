package ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.PhotoEntity

@Parcelize
class PhotoBindable(val url: String) : Parcelable {
    constructor(entity: PhotoEntity) : this(entity.url)
}