package com.tll.generalframework.base

interface IPresenter<in V : IView> {
    fun attachView(view: V)
    fun detachView(view: V)
}