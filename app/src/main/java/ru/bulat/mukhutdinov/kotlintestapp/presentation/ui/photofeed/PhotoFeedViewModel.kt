package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.bulat.mukhutdinov.kotlintestapp.domain.PhotoRepository
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.NetworkStatusEntity
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.PhotoEntity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import ru.bulat.mukhutdinov.kotlintestapp.presentation.datasource.ImagesDataSourceFactory
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainRouter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedPresenter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedView
import ru.bulat.mukhutdinov.kotlintestapp.presentation.util.Const.PAGE_SIZE

class PhotoFeedViewModel(private val photoRepository: PhotoRepository,
                         override var view: PhotoFeedView?,
                         override var router: MainRouter?) : ViewModel(), PhotoFeedPresenter {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override var entities: LiveData<PagedList<PhotoBindable>>? = null

    private lateinit var dataSourceFactory: ImagesDataSourceFactory

    private val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPrefetchDistance(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

    init {
        loadAll()
    }

    override fun loadAll() {
        load { i -> photoRepository.photos(i) }
    }

    override fun search(query: String) {
        load { i -> photoRepository.search(i, query) }
    }

    private fun load(fetchPhotos: (Int) -> Single<List<PhotoEntity>>) {
        dataSourceFactory = ImagesDataSourceFactory(compositeDisposable, fetchPhotos)

        entities = LivePagedListBuilder<Int, PhotoBindable>(dataSourceFactory, config).build()
    }

    override fun retry() {
        dataSourceFactory.liveDataSource.value?.retry()
    }

    override fun refresh() {
        dataSourceFactory.liveDataSource.value?.invalidate()
    }

    override fun getNetworkState(): LiveData<NetworkStatusEntity> {
        return Transformations.switchMap(dataSourceFactory.liveDataSource) { _ -> dataSourceFactory.liveDataSource.value?.networkState }
    }

    override fun getPhotosState(): LiveData<NetworkStatusEntity> {
        return Transformations.switchMap(dataSourceFactory.liveDataSource) { _ -> dataSourceFactory.liveDataSource.value?.initialLoad }
    }

    override fun onClick(photo: PhotoBindable, image: ImageView) {
        router?.showPhoto(photo, image)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}