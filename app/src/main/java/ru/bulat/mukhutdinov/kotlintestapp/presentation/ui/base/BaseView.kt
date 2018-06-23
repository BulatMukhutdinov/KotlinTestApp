package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base

import org.kodein.di.KodeinAware

interface BaseView<Presenter> : KodeinAware {

    val presenter: Presenter
}