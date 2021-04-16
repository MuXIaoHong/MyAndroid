package com.nan.myandroid.retrofit

import okhttp3.RequestBody
import retrofit2.http.*


/**
 *author：93289
 *date:2020/7/2
 *dsc:
 */
interface HttpMethod {

    /**
     * 提交表单
     */
    @FormUrlEncoded
    @POST("manager_house/app_api/login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): String

    /**
     * GET 加拼接查询参数
     */
    @GET("manager_house/app_api/models")
    suspend fun getShareRecordsList(@Query("page") page: Int): String

    /**
     * 传Json串
     */
    @POST("api/data?")
    fun getResult(@Body requestBody: RequestBody?):String
}