package ru.bulat.mukhutdinov.kotlintestapp.domain.entity

import ru.bulat.mukhutdinov.kotlintestapp.data.dto.NetworkStatusDto

class NetworkStatusEntity(val status: NetworkStatusDto, val message: String? = null) {

    companion object {
        var LOADED = NetworkStatusEntity(NetworkStatusDto.SUCCESS)

        var LOADING = NetworkStatusEntity(NetworkStatusDto.RUNNING)

        fun error(message: String?): NetworkStatusEntity {
            return NetworkStatusEntity(NetworkStatusDto.FAILED, message
                    ?: "unknown error")
        }
    }
}