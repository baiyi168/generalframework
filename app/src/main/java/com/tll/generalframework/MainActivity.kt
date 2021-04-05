package com.tll.generalframework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tll.generalframework.base.BaseActivity
import com.tll.generalframework.base.IView
import com.tll.generalframework.mvp.Presenter.RepoPresenter
import com.tll.generalframework.mvp.contract.RepoContract
import com.tll.generalframework.utils.CommConstants

class MainActivity : BaseActivity<RepoContract.View, RepoContract.Presenter>(),
    IView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        presenter?.loadRepos()
    }

    override fun createPresenter(): RepoPresenter {
        return RepoPresenter()
    }
}