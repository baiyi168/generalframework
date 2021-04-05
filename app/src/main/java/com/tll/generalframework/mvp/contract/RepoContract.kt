package com.tll.generalframework.mvp.contract

import com.tll.generalframework.base.IModel
import com.tll.generalframework.base.IPresenter
import com.tll.generalframework.base.IView
import com.tll.generalframework.entity.Repo
import io.reactivex.rxjava3.core.Single

interface RepoContract {
    interface View : IView {
        fun loadReposSuccess(repos: List<Repo>)
        fun loadReposFailed(code: Int, msg: String)
    }

    interface Model : IModel {
        fun loadRepos(user: String): Single<List<Repo>>
    }

    interface Presenter : IPresenter<View> {
        fun loadRepos()
    }
}