package com.zakaryan.myretailcrm.base.http.retrofit

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.zakaryan.myretailcrm.base.http.api.RetailCrmApi
import com.zakaryan.myretailcrm.base.json.JsonMapper
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

internal class RetrofitService(context: Context) {

    val okhttpClient: OkHttpClient = buildOkHttpClient(context)

    val api: RetailCrmApi = Retrofit.Builder()
        .baseUrl("https://not.used") // домен будет задаваться при вызове апи
        .client(okhttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(JsonMapper.instance))
        .build()
        .create(RetailCrmApi::class.java)

    /* ----------------------------------- COMPANION -------------------------------------------- */

    companion object {

        private const val CONNECTION_TIMEOUT_SECONDS = 30
        private const val READ_TIMEOUT_SECONDS = 30

        private fun buildOkHttpClient(context: Context): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor.Builder(context).build())
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECTION_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .connectionSpecs(getConnectionSpecList())
                .build()
        }

        private fun getConnectionSpecList(): List<ConnectionSpec> {
            val list: MutableList<ConnectionSpec> = ArrayList()
            list.add(ConnectionSpec.MODERN_TLS)
            return list
        }

    }
}