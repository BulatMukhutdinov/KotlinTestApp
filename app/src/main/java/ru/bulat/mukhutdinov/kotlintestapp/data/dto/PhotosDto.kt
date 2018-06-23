package ru.bulat.mukhutdinov.kotlintestapp.data.dto

import com.google.gson.annotations.SerializedName

data class PhotosDto(val page: Int,
                     val pages: Int,
                     @SerializedName("perpage") val perPage: Int,
                     val total: Int,
                     @SerializedName("photo") val photos: List<PhotoDto>)