package com.tll.generalframework.utils

import com.tll.generalframework.BaseApplication

class AppSystem {
    companion object {
        fun getAppVisionName(): String {
            val manager = BaseApplication.instance.packageManager;
            val info = manager.getPackageInfo(BaseApplication.instance.packageName, 0);
            val version = info.versionName;
            return version
        }
    }
}