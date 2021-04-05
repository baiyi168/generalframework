package com.tll.generalframework.base

abstract class BasePresenter<V : IView, M : IModel> : IPresenter<V> {
    var model: M? = null
    var ivew: V? = null

    override fun attachView(view: V) {
        if (model == null) {
            model = createModel()
        }
    }

    override fun detachView(view: V) {

    }

    abstract fun createModel(): M
}