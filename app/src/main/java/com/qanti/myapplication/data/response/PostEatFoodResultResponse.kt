package com.qanti.myapplication.data.response

import com.qanti.myapplication.data.model.enum.FoodCategory
import java.util.Date
import java.util.UUID

data class PostEatFoodResultResponse(
    val userId: Int,
    val type: FoodCategory,
    val weight: Int,
    val volume: Float, // 음식의 부피
    val ratio: Float, // 음식이 위 용적 대비 차지하는 비율
    val createdAt: Date, // 음식을 먹은 시간
    val complete: Date, // 소화가 완료될 시간
    val foodId: UUID,
)
