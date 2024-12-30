package com.qanti.myapplication.data.model.enum

enum class ActivityLevel(val activityLevel: String) {
    SEDENTARY("운동을 하지 않거나 거의 하지 않습니다."),
    LIGHT("주마다 1~3회 운동"),
    MODERATE("주마다 3~5회 운동"),
    VERY_ACTIVE("주마다 6~7회 운동"),
    EXTREMELY_ACTIVE("매일 고강도의 운동을 하거나 운동선수입니다."),
}