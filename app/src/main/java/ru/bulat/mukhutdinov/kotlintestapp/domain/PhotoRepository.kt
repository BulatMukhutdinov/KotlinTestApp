package ru.bulat.mukhutdinov.kotlintestapp.domain

import io.reactivex.Single
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.PhotoEntity

interface PhotoRepository {

    fun photos(page: Int): Single<List<PhotoEntity>>

    fun search(page: Int, query: String): Single<List<PhotoEntity>>
}