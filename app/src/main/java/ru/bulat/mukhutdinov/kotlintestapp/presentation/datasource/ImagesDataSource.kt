package ru.bulat.mukhutdinov.kotlintestapp.presentation.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.NetworkStatusEntity
import ru.bulat.mukhutdinov.kotlintestapp.domain.entity.PhotoEntity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.App
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class ImagesDataSource internal constructor(private val compositeDisposable: CompositeDisposable,
                                            private val fetchPhotos: (Int) -> Single<List<PhotoEntity>>)
    : PageKeyedDataSource<Int, PhotoBindable>() {

    val initialLoad: MutableLiveData<NetworkStatusEntity> = MutableLiveData()

    val networkState: MutableLiveData<NetworkStatusEntity> = MutableLiveData()

    private var retryCompletable: Completable? = null

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>,
                             callback: PageKeyedDataSource.LoadInitialCallback<Int, PhotoBindable>) {
        networkState.postValue(NetworkStatusEntity.LOADING)
        initialLoad.postValue(NetworkStatusEntity.LOADING)

        compositeDisposable.add(fetchPhotos.invoke(1)
                .flatMap { entities -> Observable.fromIterable(entities).map { entity -> PhotoBindable(entity) }.toList() }
                .subscribe({ photos ->
                    this.retryCompletable = null

                    networkState.postValue(NetworkStatusEntity.LOADED)
                    initialLoad.postValue(NetworkStatusEntity.LOADED)

                    callback.onResult(photos, null, 2)
                }, { throwable ->
                    setRetry { loadInitial(params, callback) }
                    val error = getProcessedError(throwable)
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                }))
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, PhotoBindable>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, PhotoBindable>) {
        networkState.postValue(NetworkStatusEntity.LOADING)
        compositeDisposable.add(fetchPhotos.invoke(params.key)
                .flatMap { entities -> Observable.fromIterable(entities).map { entity -> PhotoBindable(entity) }.toList() }
                .subscribe({ photos ->
                    this.retryCompletable = null

                    networkState.postValue(NetworkStatusEntity.LOADED)

                    callback.onResult(photos, params.key + 1)
                }, { throwable ->
                    setRetry { loadAfter(params, callback) }

                    networkState.postValue(getProcessedError(throwable))
                }))
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }))
        }
    }

    private fun setRetry(action: () -> Unit) {
        this.retryCompletable = Completable.fromAction(action)
    }

    private fun getProcessedError(throwable: Throwable): NetworkStatusEntity {
        val error: NetworkStatusEntity

        if (throwable is SocketTimeoutException || throwable is UnknownHostException) {
            error = NetworkStatusEntity.error(App.instance.getString(R.string.network_error))
        } else if (throwable is HttpException && throwable.response().errorBody() != null) {
            error = NetworkStatusEntity.error(throwable.response().errorBody()!!.string())
        } else {
            error = NetworkStatusEntity.error(throwable.message)
        }
        return error
    }
}