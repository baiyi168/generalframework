package com.tll.generalframework.api.interceptor

import com.tll.generalframework.utils.CommConstants
import com.tll.generalframework.utils.LogUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern

class SignInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // 基础参数
        val params: MutableMap<String, String?> = TreeMap()
        params.putAll(CommConstants.BASE_PARAMS)
        //        LogUtil.i("advertiseTAG SignInterceptor params = " + params.toString());
        if ("POST".equals(request.method, ignoreCase = true)) {
            val requestBody = request.body
            if (requestBody is MultipartBody) {
                params.putAll(GetParms(request)!!)
                val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                LogUtil.i("--params-->" + params.entries)
                for ((key, value) in params) {
                    LogUtil.i("body 中参数 $key, $value")
                    //                    builder.addFormDataPart(entry.getKey(), "", parseRequestBody(entry.getValue()));
                    if (value != null) {
                        builder.addFormDataPart(key, value)
                    }
                }
                val resbody = request.body as MultipartBody?
                val size = resbody!!.size
                for (i in 0 until size) {
                    val part = resbody.part(i)
                    LogUtil.i("--part-->$part")
                    builder.addPart(part)
                }
                val body = builder.build()
                val rawHttpUrl = request.url
                request = request.newBuilder()
                    .url(rawHttpUrl)
                    .post(body)
                    .build()
                LogUtil.i("POST 请求(MultipartBody) URL $rawHttpUrl")
            } else {
                params.putAll(parsePost(request)!!)
                val builder = FormBody.Builder()
                for ((key, value) in params) {
                    if (value != null) {
                        builder.add(key, value)
                    }
                    //                LogUtil.i("body 中参数 " + entry.getKey() + ", " + entry.getValue());
                }
                val body = builder.build()
                val rawHttpUrl = request.url
                request = request.newBuilder()
                    .url(rawHttpUrl)
                    .post(body)
                    .build()
                LogUtil.i("POST 请求 URL: $rawHttpUrl")
            }

            // 添加签名
            request = request.newBuilder()
                .build()
            printBodyStr(request)
        } else if ("GET".equals(request.method, ignoreCase = true)) {
            val rawHttpUrl = request.url
            val builder = rawHttpUrl.newBuilder()

            // 添加基础参数
            for ((key, value) in params) {
                builder.addQueryParameter(key, value)
            }
            val newUrl = builder.build()
            request = request.newBuilder()
                .url(newUrl)
                .build()

            // 获取参数对
            val httpURlWithParams = request.url
            val strings = httpURlWithParams.queryParameterNames
            val params1: MutableMap<String, String?> = TreeMap()
            for (next in strings) {
                val `val` = httpURlWithParams.queryParameter(next)
                params1[next] = `val`
                if ("n".equals(next, ignoreCase = true) && "1".equals(`val`, ignoreCase = true)) {
                    LogUtil.e("未签名校验")
                }
            }
            LogUtil.i("GET 请求 URL: $newUrl")
            request = request.newBuilder()
                .build()
        }
        return chain.proceed(request)
    }


    fun parseRequestBody(value: String?): RequestBody? {
        return value?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
    }

    // 查看 post 参数
    @Throws(IOException::class)
    private fun printBodyStr(request: Request) {
        val bs = Buffer()
        request.body!!.writeTo(bs)
        val s = bs.readUtf8()
        LogUtil.i("printBodyStr s = $s")
    }

    /**
     * 获取表单中的参数
     *
     * @param request
     * @return
     */
    private fun GetParms(request: Request): Map<out String, String?>? {
        var map: TreeMap<String, String?>? = null
        val bs_old = Buffer()
        try {
            request.body!!.writeTo(bs_old)
            val str = bs_old.readUtf8()
            if (str.isEmpty()) {
                return map
            }
            map = TreeMap()
            val split = str.split("--").toTypedArray()
            for (s in split) {
                LogUtil.i("GetParms body s = $s")
                val split1 = s.split("\n").toTypedArray()
                if (split1.size > 2) {
                    val Key = split1[1]
                    val splitKey = Key.split("=").toTypedArray()
                    if (splitKey.size == 2) {
                        val key = splitKey[1].replace("\"", "").replace("\r", "")
                        LogUtil.i("GetParms body concrectkey " + key + "--value--" + split1[split1.size - 1])
                        if (!isSpecialChar(key) && key != "file") {
                            map[key] = split1[split1.size - 1].replace("\r", "").replace("\"", "")
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return map
    }

    // 获取 post 参数
    @Throws(IOException::class)
    private fun parsePost(request: Request): Map<String, String?>? {
        val bs_old = Buffer()
        request.body!!.writeTo(bs_old)
        val str = bs_old.readUtf8()
        LogUtil.i("body bs_old = $str")
        val map: MutableMap<String, String?> = HashMap()
        if (str.isEmpty()) {
            return map
        }
        val split = str.split("&").toTypedArray()
        for (s in split) {
            val keyVal = s.split("=").toTypedArray()
            if (keyVal.size == 2) {
                map[keyVal[0]] = keyVal[1]
            }
        }
        return map
    }

    fun isSpecialChar(str: String?): Boolean {
        val regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.find()
    }
}