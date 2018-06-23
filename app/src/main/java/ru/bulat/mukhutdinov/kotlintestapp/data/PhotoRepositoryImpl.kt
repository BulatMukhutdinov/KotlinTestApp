package ru.bulat.mukhutdinov.kotlintestapp.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.Exceptions
import ru.bulat.mukhutdinov.kotlintestapp.data.dto.ResponseDto
import ru.bulat.mukhutdinov.kotlintestapp.data.network.FlickrApiService
import ru.bulat.mukhutdinov.kotlintestapp.domain.PhotoRepository
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.PhotoEntity

class PhotoRepositoryImpl : PhotoRepository {

    companion object {
        const val FAIL_STATUS = "fail"
    }

    override fun photos(page: Int): Single<List<PhotoEntity>> {
        return transformToEntityList(FlickrApiService.create().photos(page))
    }

    override fun search(page: Int, query: String): Single<List<PhotoEntity>> {
        return transformToEntityList(FlickrApiService.create().search(page, query))
    }

    private fun transformToEntityList(photoResponse: Single<ResponseDto>): Single<List<PhotoEntity>> {
        return photoResponse
                .map { response ->
                    if (response.stat == FAIL_STATUS)
                        throw Exceptions.propagate(RuntimeException(response.message))
                    else
                        response
                }
                .map { response -> response.photos.photos }
                .flatMap { list -> Observable.fromIterable(list).map { dto -> PhotoEntity(dto) }.toList() }
    }
}