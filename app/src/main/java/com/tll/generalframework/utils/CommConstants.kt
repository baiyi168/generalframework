package com.tll.generalframework.utils

class CommConstants {
    companion object {
        const val BASE_URL = "https://api.github.com/"
        val BASE_PARAMS = mapOf("appVersion" to AppSystem.getAppVisionName())
    }
}
