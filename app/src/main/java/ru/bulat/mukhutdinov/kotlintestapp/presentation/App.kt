package ru.bulat.mukhutdinov.kotlintestapp.presentation

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton
import ru.bulat.mukhutdinov.kotlintestapp.data.PhotoRepositoryImpl
import ru.bulat.mukhutdinov.kotlintestapp.domain.PhotoRepository
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainRouter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.PhotoPresenterImpl
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.contract.PhotoPresenter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.contract.PhotoView
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.PhotoFeedViewModel
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.PhotoFeedViewModelFactory
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedPresenter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedView


class App : Application(), KodeinAware {

    companion object Instance {
        lateinit var instance: App
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override val kodein = Kodein.lazy {

        bind<PhotoRepository>() with singleton {
            PhotoRepositoryImpl()
        }

        bind<PhotoPresenter>() with scoped(AndroidLifecycleScope<Fragment>()).singleton {
            PhotoPresenterImpl(context as PhotoView, context.activity as MainRouter)
        }

        bind<PhotoFeedPresenter>() with scoped(AndroidLifecycleScope<Fragment>()).singleton {
            ViewModelProviders.of(context, PhotoFeedViewModelFactory(
                    context as PhotoFeedView,
                    context.activity as MainRouter,
                    instance())).get(PhotoFeedViewModel::class.java)
        }
    }
}