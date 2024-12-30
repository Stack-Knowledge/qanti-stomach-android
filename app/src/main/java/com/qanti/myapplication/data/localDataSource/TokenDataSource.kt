package com.qanti.myapplication.data.localDataSource

import java.util.UUID

interface TokenDataSource {
    suspend fun getToken(): UUID?
    suspend fun setToken(token: UUID)
    suspend fun setUserId(id: Int)
    suspend fun getUserId(): Int?
}