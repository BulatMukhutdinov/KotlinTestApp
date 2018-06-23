package ru.bulat.mukhutdinov.kotlintestapp.presentation.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.PhotoEntity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable


class ImagesDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                              private val fetchPhotos: (Int) -> Single<List<PhotoEntity>>)
    : DataSource.Factory<Int, PhotoBindable>() {

    val liveDataSource: MutableLiveData<ImagesDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, PhotoBindable> {
        val dataSource = ImagesDataSource(compositeDisposable, fetchPhotos)
        liveDataSource.postValue(dataSource)
        return dataSource
    }
}