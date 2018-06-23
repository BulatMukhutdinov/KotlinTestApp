package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base

import androidx.fragment.app.Fragment
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.kcontext

abstract class BaseFragment<Presenter : BasePresenter<View, Router>,
        View : BaseView<Presenter>, Router : BaseRouter> : Fragment(), BaseView<Presenter> {

    override val kodein by closestKodein()

    override val kodeinContext = kcontext<Fragment>(this)

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}