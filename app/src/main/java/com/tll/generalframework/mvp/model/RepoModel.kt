package com.tll.generalframework.mvp.model

import com.tll.generalframework.api.ApiFactory
import com.tll.generalframework.entity.Repo
import com.tll.generalframework.mvp.contract.RepoContract
import io.reactivex.rxjava3.core.Single

class RepoModel : RepoContract.Model {
    override fun loadRepos(user: String): Single<List<Repo>> {
        val listRepos = ApiFactory.sInstance.getApiService().listRepos(user)
        return listRepos;
    }

}