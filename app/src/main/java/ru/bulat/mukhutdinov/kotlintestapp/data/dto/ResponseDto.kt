package ru.bulat.mukhutdinov.kotlintestapp.data.dto

data class ResponseDto(val photos: PhotosDto,
                       val stat: String,
                       val message: String)