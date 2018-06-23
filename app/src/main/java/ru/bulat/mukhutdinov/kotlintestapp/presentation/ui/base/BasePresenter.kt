package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base

import androidx.annotation.CallSuper

interface BasePresenter<View, Router>  {

    var view: View?

    var router: Router?

    @CallSuper
    fun onDestroy() {
        view = null
        router = null
    }

}