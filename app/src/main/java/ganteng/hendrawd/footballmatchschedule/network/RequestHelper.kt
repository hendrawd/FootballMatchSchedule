package ganteng.hendrawd.footballmatchschedule.network

import ganteng.hendrawd.footballmatchschedule.BuildConfig.DEBUG
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * @author hendrawd on 19/07/18
 */

val String.getDataAsString get() = RequestHelper.getDataAsString(this)
val String.getDataAsJSONObject get() = RequestHelper.getDataAsJSONObject(this)
fun String.getData(callback: Callback) = RequestHelper.getData(this, callback)
fun String.postData(jsonString: String, callback: Callback? = null) = RequestHelper.postData(this, jsonString, callback)

object RequestHelper {
    private val MEDIA_TYPE_JSON by lazy { MediaType.parse("application/json; charset=utf-8") }
    private val OK_HTTP_LOGGING_INTERCEPTOR by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
                if (DEBUG) {
                    BODY
                } else {
                    NONE
                }
        loggingInterceptor
    }
    private val OK_HTTP_CLIENT by lazy {
        OkHttpClient.Builder()
                .addInterceptor(OK_HTTP_LOGGING_INTERCEPTOR)
                .build()
    }

    fun getDataAsString(url: String): String? {
        try {
            val request = Request.Builder()
                    .url(url)
                    .build()
            val response = OK_HTTP_CLIENT.newCall(request).execute()
            val responseBody = response.body()
            return responseBody?.string()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
        return null
    }

    fun getDataAsJSONObject(url: String): JSONObject? {
        val data = getDataAsString(url)
        if (data != null) {
            try {
                return JSONObject(data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getData(url: String, callback: Callback) {
        val request = Request.Builder()
                .url(url)
                .build()
        OK_HTTP_CLIENT.newCall(request).enqueue(callback)
    }

    fun postData(url: String, json: String, callback: Callback? = null): String? {
        val body = RequestBody.create(MEDIA_TYPE_JSON, json)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()
        if (callback == null) {
            try {
                val response = OK_HTTP_CLIENT.newCall(request).execute()
                val responseBody = response.body()
                return responseBody?.string()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        } else {
            OK_HTTP_CLIENT.newCall(request).enqueue(callback)
        }
        return null
    }
}
