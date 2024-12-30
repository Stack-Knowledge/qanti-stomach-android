package com.qanti.myapplication.data.api

import com.qanti.myapplication.data.request.PostEatFoodRequest
import com.qanti.myapplication.data.request.SignUpRequest
import com.qanti.myapplication.data.response.PostEatFoodResultResponse
import com.qanti.myapplication.data.response.SignUpResponse
import com.qanti.myapplication.data.response.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface UserAPI {
    @POST("users")
    suspend fun signUp(
        @Body body: SignUpRequest
    ): SignUpResponse

    @GET("users/me")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): UserInfoResponse

    @POST("stomach/{id}")
    suspend fun postEatFoodInfo(
        @Body body: PostEatFoodRequest,
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): List<PostEatFoodResultResponse>

    @GET("stomach/{id}")
    suspend fun getStomachInfo(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): List<PostEatFoodResultResponse>
}