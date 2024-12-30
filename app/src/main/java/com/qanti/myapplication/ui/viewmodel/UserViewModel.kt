package com.qanti.myapplication.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qanti.myapplication.data.datasource.UserDataSource
import com.qanti.myapplication.data.localDataSource.TokenDataSource
import com.qanti.myapplication.data.localDataSource.TokenDataSourceImpl
import com.qanti.myapplication.data.request.PostEatFoodRequest
import com.qanti.myapplication.data.request.SignUpRequest
import com.qanti.myapplication.data.response.PostEatFoodResultResponse
import com.qanti.myapplication.data.response.SignUpResponse
import com.qanti.myapplication.data.response.UserInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val tokenDataSource: TokenDataSource,
) : ViewModel() {
    private val _signUpResponse = MutableStateFlow<SignUpResponse?>(null)
    val signUpResponse: StateFlow<SignUpResponse?> get() = _signUpResponse.asStateFlow()

    private val _userInfoResponse = MutableStateFlow<UserInfoResponse?>(null)
    val userInfoResponse: StateFlow<UserInfoResponse?> get() = _userInfoResponse.asStateFlow()

    private val _eatFoodResultResponse =
        MutableStateFlow<List<PostEatFoodResultResponse>>(emptyList())
    val eatFoodResultResponse: StateFlow<List<PostEatFoodResultResponse>> get() = _eatFoodResultResponse.asStateFlow()

    private val _stomachInfoResponse = MutableStateFlow<PostEatFoodResultResponse?>(null)
    val stomachInfoResponse: StateFlow<PostEatFoodResultResponse?> get() = _stomachInfoResponse.asStateFlow()

    fun requestSignUp(
        body: SignUpRequest,
    ) = viewModelScope.launch {
        userDataSource.signUp(
            body = body
        ).collect { response ->
            _signUpResponse.update { response }
            tokenDataSource.setToken(response.token)
            requestGetUserInfo()
        }
    }

    fun requestGetUserInfo() = viewModelScope.launch {
        tokenDataSource.getToken()?.let { token ->
            userDataSource.getUserInfo(token).collect { response ->
                _userInfoResponse.update { response }
                tokenDataSource.setUserId(response.id)
            }
        }
    }

    fun requestPostEatFoodInfo(
        body: PostEatFoodRequest,
    ) = viewModelScope.launch {
        userDataSource.postEatFoodInfo(
            body = body,
            userId = tokenDataSource.getUserId()!!,
            token = tokenDataSource.getToken()!!
        ).collect { response ->
            _eatFoodResultResponse.update { response }
        }
    }

    fun requestGetStomachInfo() = viewModelScope.launch {
        if (getUserId() != null && tokenDataSource.getToken() != null) {
            tokenDataSource.getUserId()?.let {
                userDataSource.getStomachInfo(
                    userId = it,
                    token = tokenDataSource.getToken()!!
                ).collect { response ->
                    _stomachInfoResponse.update { response.first() }
                }
            }
        }
    }

    suspend fun getUserId(): Int? {
        val userId = tokenDataSource.getUserId()

        return userId
    }

    suspend fun checkLocalToken(): Boolean? {
        val token = tokenDataSource.getToken()

        return if (token != null) true else null
    }
}