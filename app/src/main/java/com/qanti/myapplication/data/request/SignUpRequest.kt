package com.qanti.myapplication.data.request

import com.qanti.myapplication.data.model.enum.ActivityLevel
import com.qanti.myapplication.data.model.enum.Gender

data class SignUpRequest(
    val name: String,
    val email: String,
    val phone: String?,
    val weight: Int,
    val height: Int,
    val age: Int,
    val gender: Gender,
    val activityLevel: ActivityLevel,
    val mealFrequency: Int,

)
