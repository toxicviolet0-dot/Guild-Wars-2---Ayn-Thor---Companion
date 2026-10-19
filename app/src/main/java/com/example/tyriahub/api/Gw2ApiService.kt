package com.example.tyriahub.api

import com.example.tyriahub.model.AccountResponse
import com.example.tyriahub.model.CharacterResponse
import com.example.tyriahub.model.CommercePriceResponse
import com.example.tyriahub.model.TokenInfoResponse
import com.example.tyriahub.model.WalletItemResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface Gw2Api {
    @GET("v2/tokeninfo")
    suspend fun getTokenInfo(
        @Header("Authorization") authHeader: String
    ): TokenInfoResponse

    @GET("v2/account")
    suspend fun getAccount(
        @Header("Authorization") authHeader: String
    ): AccountResponse

    @GET("v2/account/wallet")
    suspend fun getWallet(
        @Header("Authorization") authHeader: String
    ): List<WalletItemResponse>

    @GET("v2/characters")
    suspend fun getCharacterNames(
        @Header("Authorization") authHeader: String
    ): List<String>

    @GET("v2/characters/{name}")
    suspend fun getCharacter(
        @Path("name") characterName: String,
        @Header("Authorization") authHeader: String
    ): CharacterResponse

    @GET("v2/commerce/prices/{id}")
    suspend fun getItemPrice(
        @Path("id") itemId: Int
    ): CommercePriceResponse
}

object Gw2ApiClient {
    private const val BASE_URL = "https://api.guildwars2.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .dispatcher(okhttp3.Dispatcher().apply {
            maxRequests = 32
            maxRequestsPerHost = 20
        })
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val api: Gw2Api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(Gw2Api::class.java)
}
