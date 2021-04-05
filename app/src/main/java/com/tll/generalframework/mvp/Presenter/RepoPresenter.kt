package com.tll.generalframework.mvp.Presenter

import com.tll.generalframework.base.BasePresenter
import com.tll.generalframework.mvp.contract.RepoContract
import com.tll.generalframework.mvp.model.RepoModel
import com.tll.generalframework.utils.LogUtil

class RepoPresenter : BasePresenter<RepoContract.View, RepoContract.Model>(),
    RepoContract.Presenter {
    override fun loadRepos() {
        var listRepoSingle = model?.loadRepos("octocat")
        listRepoSingle?.subscribe({ listRepos ->
            LogUtil.d(listRepos[0].name)
        }) { throwable ->
            LogUtil.e(throwable.toString())
        }
    }

    override fun createModel(): RepoContract.Model {
        return RepoModel()
    }
}