package ru.bulat.mukhutdinov.kotlintestapp.data.dto

import com.google.gson.annotations.SerializedName

data class PhotoDto(val id: Long,
                    val owner: String,
                    val secret: String,
                    val server: Int,
                    val farm: Int,
                    val title: String,
                    @SerializedName("ispublic") val isPublic: Int,
                    @SerializedName("isfriend") val isFriend: Int,
                    @SerializedName("isfamily") val isFamily: Int)