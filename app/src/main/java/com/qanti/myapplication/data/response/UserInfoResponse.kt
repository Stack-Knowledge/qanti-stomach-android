package com.qanti.myapplication.data.response

import com.qanti.myapplication.data.model.enum.ActivityLevel
import com.qanti.myapplication.data.model.enum.Gender
import java.util.Date
import java.util.UUID

data class UserInfoResponse(
        val id: Int,
        val createdAt: Date,
        val updateAt: Date?,
        val name: String,
        val email: String,
        val phone: String?,
        val weight: Int,
        val height: Int,
        val age: Int,
        val gender: Gender, // 성별
        val activityLevel: ActivityLevel, // 활동량
        val mealFrequency: Int, // 하루에 밥 몇번 먹는지 식사 횟수
        val BMR: Float? = null, // 기초대사량
        val TDEE: Float? = null, // 활동했을때 소모되는 대사량
        val stomachVolume: Int? = null, // 배 용적량
        val token: UUID,
    )