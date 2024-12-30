package com.qanti.myapplication.data.request

import com.qanti.myapplication.data.model.enum.FoodCategory

data class PostEatFoodRequest(
    val type: FoodCategory,
    val weight: Int,
)
