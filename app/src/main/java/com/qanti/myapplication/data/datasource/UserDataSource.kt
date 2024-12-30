package com.qanti.myapplication.data.datasource

import com.qanti.myapplication.data.request.PostEatFoodRequest
import com.qanti.myapplication.data.request.SignUpRequest
import com.qanti.myapplication.data.response.PostEatFoodResultResponse
import com.qanti.myapplication.data.response.SignUpResponse
import com.qanti.myapplication.data.response.UserInfoResponse
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface UserDataSource {
    suspend fun signUp(body: SignUpRequest): Flow<SignUpResponse>
    suspend fun postEatFoodInfo(body: PostEatFoodRequest, userId: Int, token: UUID): Flow<List<PostEatFoodResultResponse>>
    suspend fun getStomachInfo(userId: Int, token: UUID): Flow<List<PostEatFoodResultResponse>>
    suspend fun getUserInfo(token: UUID): Flow<UserInfoResponse>
}