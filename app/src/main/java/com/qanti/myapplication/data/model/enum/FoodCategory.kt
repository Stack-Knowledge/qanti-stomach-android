package com.qanti.myapplication.data.model.enum

import com.google.gson.annotations.SerializedName

    enum class FoodCategory(val foodCategory: String) {
        @SerializedName("밥/곡물류") 밥_곡물류("밥/곡물류"),
        @SerializedName("면/파스타류") 면_파스타류("면/파스타류"),
        @SerializedName("빵/베이커리류") 빵_베이커리류("빵/베이커리류"),
        @SerializedName("고기류") 고기류("고기류"),
        @SerializedName("해산물류") 해산물류("해산물류"),
        @SerializedName("채소/샐러드류") 채소_샐러드류("채소/샐러드류"),
        @SerializedName("과일류") 과일류("과일류"),
        @SerializedName("스프/국류") 스프_국류("스프/국류"),
        @SerializedName("음료") 음료("음료"),
        @SerializedName("스낵/간식류") 스낵_간식류("스낵/간식류"),
        @SerializedName("튀김류") 튀김류("튀김류"),
        @SerializedName("디저트류") 디저트류("디저트류"),
        @SerializedName("유제품") 유제품("유제품")
    }
