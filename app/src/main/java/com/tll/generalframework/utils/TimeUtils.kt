package com.tll.generalframework.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun createSysTimeFileName(): String {
            val formatter = SimpleDateFormat("yyyyMMddHHmmss")
            val curDate = Date(System.currentTimeMillis()) //获取当前时间
            return formatter.format(curDate) + ".txt"
        }
    }
}