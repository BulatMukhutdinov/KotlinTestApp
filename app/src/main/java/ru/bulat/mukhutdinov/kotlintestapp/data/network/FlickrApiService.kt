package ru.bulat.mukhutdinov.kotlintestapp.data.network

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.data.dto.ResponseDto
import ru.bulat.mukhutdinov.kotlintestapp.presentation.App


interface FlickrApiService {

    @GET("?method=flickr.photos.getRecent&format=json&nojsoncallback=1")
    fun photos(@Query("page") page: Int): Single<ResponseDto>

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1")
    fun search(@Query("page") page: Int, @Query("text") text: String): Single<ResponseDto>

    companion object {

        private const val PHOTOS_PER_PAGE = "50"

        fun create(): FlickrApiService {

            val client = OkHttpClient.Builder().addInterceptor { chain ->
                var request = chain.request()
                val url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("api_key", App.instance.getString(R.string.api_key))
                        .addQueryParameter("per_page", PHOTOS_PER_PAGE)
                        .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }.build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl(App.instance.getString(R.string.server_url))
                    .client(client)
                    .build()

            return retrofit.create(FlickrApiService::class.java)
        }
    }
}