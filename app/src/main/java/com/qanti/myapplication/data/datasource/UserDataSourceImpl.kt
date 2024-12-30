package com.qanti.myapplication.data.datasource

import com.qanti.myapplication.data.QantiStomachAPIHandler
import com.qanti.myapplication.data.api.UserAPI
import com.qanti.myapplication.data.request.PostEatFoodRequest
import com.qanti.myapplication.data.request.SignUpRequest
import com.qanti.myapplication.data.response.PostEatFoodResultResponse
import com.qanti.myapplication.data.response.SignUpResponse
import com.qanti.myapplication.data.response.UserInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userAPI: UserAPI,
) : UserDataSource {

    override suspend fun signUp(body: SignUpRequest): Flow<SignUpResponse> =
        makeRequest { userAPI.signUp(body = body) }

    override suspend fun getUserInfo(token: UUID): Flow<UserInfoResponse> =
        makeRequest { userAPI.getUserInfo(token = "Bearer $token") }

    override suspend fun postEatFoodInfo(body: PostEatFoodRequest, userId: Int, token: UUID) =
        makeRequest { userAPI.postEatFoodInfo(body = body, id = userId, token = "Bearer $token") }

    override suspend fun getStomachInfo(userId: Int, token: UUID): Flow<List<PostEatFoodResultResponse>> =
        makeRequest { userAPI.getStomachInfo(id = userId, token = "Bearer $token") }


}

private inline fun <reified T> makeRequest(crossinline apiCall: suspend () -> T): Flow<T> = flow {
    emit(
        QantiStomachAPIHandler<T>()
            .httpRequest { apiCall() }
            .sendRequest()
    )
}.flowOn(Dispatchers.IO)