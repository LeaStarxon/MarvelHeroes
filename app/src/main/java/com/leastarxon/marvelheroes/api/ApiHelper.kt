package com.leastarxon.marvelheroes.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.leastarxon.marvelheroes.model.MarvelCharactersListResponse
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

class ApiHelper {
    private val OTHER_ERROR: Int = 1022
    private var client: OkHttpClient? = null
    private val LOGGING_LEVEL: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
    private val open_key: String = ""
    private val close_key: String = ""
    val BASE_URL = "https://gateway.marvel.com/"

    fun getFSMClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = LOGGING_LEVEL
        if (client == null) {
            client = Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .followRedirects(false)
                .build()
        }
        return client!!
    }

    private fun defaultRetrofit(): Retrofit? {
        val gson = GsonBuilder().serializeNulls().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getFSMClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun loadCharactersList(offset: Int): Single<Any> {
        return try {
            val service = defaultRetrofit()!!.create(IApiHelper::class.java)
            val timeStamp = System.currentTimeMillis().toString()
            val mD5 = md5(timeStamp)
            if (mD5 != null) {
                val load: Single<Any?>? = Single.create { answer: SingleEmitter<Any?> ->
                    service.loadHeroes(
                        timeStamp,
                        open_key,
                        mD5,
                        "name",
                        100,
                        offset
                        )
                        ?.subscribe({ answer.onSuccess(it) }, { answer.onError(it) })
                }
                if (load == null) {
                    returnSingleError()
                } else {
                    Single.create { answer: SingleEmitter<Any> ->
                        load.subscribe(
                            { data: Any? ->
                                if (data != null) {
                                    var throwable: Throwable? = null
                                    var response: MarvelCharactersListResponse? = null
                                    try {
                                        response = data as? MarvelCharactersListResponse
                                        throwable = if (response?.code == "200") null else {
                                            Throwable(
                                                response?.code?.toString() ?: "Error from server"
                                            )
                                        }
                                    } catch (ex: Exception) {
                                        throwable = Throwable(ex.message)
                                    }
                                    if (throwable == null) {
                                        if (response != null) {
                                            answer.onSuccess(response)
                                        } else {
                                            val t = Throwable("Error type")
                                            if (!answer.isDisposed) {
                                                answer.onError(t)
                                            }
                                        }
                                    } else {
                                        if (!answer.isDisposed) {
                                            answer.onError(throwable)
                                        }
                                    }
                                } else {
                                    if (!answer.isDisposed) {
                                        answer.onError(Throwable("Null server answer"))
                                    }
                                }
                            }
                        ) { t: Throwable ->
                            if (!answer.isDisposed) {
                                answer.onError(t)
                            }
                        }
                    }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                }
            } else {
                returnSingleError()
            }
        } catch (ex: Exception) {
            returnSingleError()
        }

    }

    private fun returnSingleError(): Single<Any> {
        return Single.create { e: SingleEmitter<Any> ->
            e.onError(Throwable("Error type"))
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getMD5(string: String): String? {
        val result = string + close_key+ open_key
            try {
            val md = MessageDigest.getInstance("MD5")
            val padStart =
                BigInteger(1, md.digest(result.toByteArray())).toString(16).padStart(32, '0')
            return padStart
        } catch (e: NoSuchAlgorithmException) {
            Log.d("MD5.ERROR", e.toString())
        }
        return null
    }
    fun md5(s: String): String? {
        val result = s + close_key+ open_key
        val MD5 = "MD5"
        try { // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(MD5)
            digest.update(result.toByteArray())
            val messageDigest = digest.digest()
            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }
}