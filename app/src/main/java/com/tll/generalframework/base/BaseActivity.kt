package com.tll.generalframework.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<in V : IView, T : IPresenter<V>> : AppCompatActivity(), IView {
    var presenter: T? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        presenter = createPresenter()
        presenter?.attachView(this as V)
    }

    abstract fun createPresenter(): T

    override fun onDestroy() {
        presenter?.detachView(this as V)
        super.onDestroy()
    }
}