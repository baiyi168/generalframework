package com.tll.generalframework.utils

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.File

class LogUtil {
    companion object {
        private var sTag = "LogUtil"
        private var sIsDebug = true
        private var sIsTrace = false

        /**
         * 配置 LogUtil
         *
         * @param tag     默认 tag
         * @param isDebug 是否显示
         * @param isTrace 是否显示详细
         */
        fun init(tag: String, isDebug: Boolean, isTrace: Boolean) {
            sTag = tag
            sIsDebug = isDebug
            sIsTrace = isTrace
        }

        fun d(msg: Any, tag: String = "") {
            if (sIsDebug) {
                if (TextUtils.isEmpty(tag)) {
                    Log.d(sTag, getTraceMsg() + msg)
                } else {
                    Log.d(sTag + "_" + tag, getTraceMsg() + msg)
                }
            }
        }

        fun i(msg: Any, tag: String = "") {
            if (sIsDebug) {
                if (TextUtils.isEmpty(tag)) {
                    Log.i(sTag, getTraceMsg() + msg)
                } else {
                    Log.i(sTag + "_" + tag, getTraceMsg() + msg)
                }
            }
        }

        fun w(msg: Any, tag: String = "") {
            if (sIsDebug) {
                if (TextUtils.isEmpty(tag)) {
                    Log.w(sTag, getTraceMsg() + msg)
                } else {
                    Log.w(sTag + "_" + tag, getTraceMsg() + msg)
                }
            }
        }

        fun e(msg: Any, tag: String = "") {
            if (sIsDebug) {
                if (TextUtils.isEmpty(tag)) {
                    Log.e(sTag, getTraceMsg() + msg)
                } else {
                    Log.e(sTag + "_" + tag, getTraceMsg() + msg)
                }
            }
        }

        /**获取栈调用日志信息*/
        private fun getTraceMsg(): String {
            var msg = ""
            if (sIsTrace) {
                val list = Thread.currentThread().stackTrace
                val sb = StringBuilder()
                sb.append("[")
                if (list.size > 3) {
                    sb.append(list[3].fileName + "_" + list[3].methodName + "_" + list[3].lineNumber + "_")
                }
                if (list.size > 4) {
                    sb.append(list[4].fileName + "_" + list[4].methodName + "_" + list[4].lineNumber + "_")
                }
                if (list.size > 5) {
                    sb.append(list[5].fileName + "_" + list[5].methodName + "_" + list[5].lineNumber)
                }
                sb.append("] ")
                msg = sb.toString()
            }
            return msg
        }
    }
}