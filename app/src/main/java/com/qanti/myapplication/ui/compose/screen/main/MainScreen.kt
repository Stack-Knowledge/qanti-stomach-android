package com.qanti.myapplication.ui.compose.screen.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qanti.myapplication.data.model.enum.ActivityLevel
import com.qanti.myapplication.data.model.enum.FoodCategory
import com.qanti.myapplication.data.model.enum.Gender
import com.qanti.myapplication.data.request.PostEatFoodRequest
import com.qanti.myapplication.data.response.PostEatFoodResultResponse
import com.qanti.myapplication.data.response.UserInfoResponse
import com.qanti.myapplication.ui.viewmodel.UserViewModel
import java.time.Instant
import java.time.LocalDate
import java.util.Date
import java.util.UUID

const val ROUTE_MAIN = "route_main"

@SuppressLint("StateFlowValueCalledInComposition")
fun NavGraphBuilder.mainScreen() {

    composable(ROUTE_MAIN) {
        val userViewModel = hiltViewModel<UserViewModel>()
        val userInfo = userViewModel.userInfoResponse.collectAsStateWithLifecycle().value
        val postEatFoodResultResponse = userViewModel.eatFoodResultResponse.collectAsStateWithLifecycle().value
        val stomachInfoResponse = userViewModel.stomachInfoResponse.collectAsStateWithLifecycle().value
        
        LaunchedEffect(Unit) {
            userViewModel.requestGetUserInfo()
            userViewModel.requestGetStomachInfo()
        }

        if (stomachInfoResponse != null) {
            MainScreen(
                userInfo = userInfo,
                postEatFoodResultResponse = postEatFoodResultResponse,
                onPostFoodClick = { postEatFoodRequest ->
                    userViewModel.requestPostEatFoodInfo(
                        body = postEatFoodRequest,
                    )
                    userViewModel.requestGetStomachInfo()
                },
                stomachInfoResponse = stomachInfoResponse
            )
        }
    }
}

@Composable
fun MainScreen(
    userInfo: UserInfoResponse?,
    postEatFoodResultResponse: List<PostEatFoodResultResponse>,
    stomachInfoResponse: PostEatFoodResultResponse,
    onPostFoodClick: (PostEatFoodRequest) -> Unit,
) {
    var foodCategory by remember { mutableStateOf(FoodCategory.음료) }
    var foodCategoryExpanded by remember { mutableStateOf(false) }
    var eatFoodWeight by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)

        ) {
            if (userInfo != null) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .background(Color.Gray, RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "이름 : ${userInfo.name}",
                                color = Color.Yellow,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "나이 : ${userInfo.age}세",
                                color = Color.Yellow
                            )

                            Text(
                                text = if (userInfo.gender == Gender.MALE) "성별 : 남성" else "성별 : 여성",
                                color = Color.Yellow
                            )

                            Text(
                                text = "몸무게 : ${userInfo.weight}kg",
                                color = Color.Yellow
                            )

                            Text(
                                text = "키 : ${userInfo.height}cm",
                                color = Color.Yellow
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                pickFoodCategoryMenuComponent(
                    foodCategoryExpanded = foodCategoryExpanded,
                    foodCategory = foodCategory,
                    text = eatFoodWeight,
                    onFoodCategoryExpandedChange = {
                        foodCategoryExpanded = it
                    },
                    onFoodCategoryChange = {
                        foodCategory = it
                    },
                    onTextChange = {
                        eatFoodWeight = it
                    },
                    onPostFoodClick = {
                        Log.e("foodCategory", foodCategory.toString())
                        Log.e("eatFoodWeight", eatFoodWeight.toInt().toString())
                        onPostFoodClick(
                            PostEatFoodRequest(
                                type = foodCategory,
                                weight = eatFoodWeight.toInt()
                            )
                        )
                    }
                )

                currentFoodStatusComponent(
                    postEatFoodResultResponse = postEatFoodResultResponse
                )

                totalStomachStatusComponent(
                    stomachInfoResponse = stomachInfoResponse
                )
            }
        }
    }
}

fun LazyListScope.totalStomachStatusComponent(
    stomachInfoResponse: PostEatFoodResultResponse,
) {
    item {
        val statusMessage = when {
            stomachInfoResponse.ratio.toInt() > 100 -> "위 용적량을 넘었어요! 과식을 주의하세요"
            stomachInfoResponse.ratio.toInt() in 90..100 -> "배가 꽉 찬 상태에요! 추가 섭취는 자제하세요."
            stomachInfoResponse.ratio.toInt() in 80..89 -> "배가 아주 부른 상태에요."
            stomachInfoResponse.ratio.toInt() in 70..79 -> "배가 부를 정도로 먹었어요."
            stomachInfoResponse.ratio.toInt() in 50..69 -> "적당히 먹은 상태에요."
            stomachInfoResponse.ratio.toInt() in 30..49 -> "아직 약간 배가 고픈 상태에요."
            stomachInfoResponse.ratio.toInt() in 10..29 -> "아직 충분히 먹을 수 있어요."
            stomachInfoResponse.ratio.toInt() in 1..9 -> "거의 안 먹은 상태에요. 식사를 시작해보세요!"
            else -> "위가 비어 있어요. 식사를 시작하세요!"
        }

        Box(
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = "현재 내 뱃속 상태",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Yellow
                )

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "뱃속 음식의 부피: ${stomachInfoResponse.volume}cm³\n뱃속 음식의 무게: ${stomachInfoResponse.weight}g\n뱃속 음식의 위 차지 용적량: ${stomachInfoResponse.ratio}%\n먹은시간 : ${stomachInfoResponse.createdAt}\n소화 완료 예정 시간 : ${stomachInfoResponse.complete}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Yellow
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = statusMessage,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Yellow
                        )
                    }
                }
            }
        }
    }
}


fun LazyListScope.currentFoodStatusComponent(
    postEatFoodResultResponse: List<PostEatFoodResultResponse>,
) {
    itemsIndexed(postEatFoodResultResponse) { index, postEatFoodResult ->
        Box(
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "위에 들어온 음식 정보",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Yellow,
            )
        }
        Box(
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .background(Color.DarkGray, RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "들어온 순서: ${index + 1}\n먹은 음식:${postEatFoodResult.type.foodCategory} \n무게:${postEatFoodResult.weight}g \n부피:${postEatFoodResult.volume}cm³ \n부피 대비 위 차지 용적량:${postEatFoodResult.ratio}%",
                        color = Color.Yellow,
                        style = MaterialTheme.typography.labelSmall
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(7.dp))
    }
}

fun LazyListScope.pickFoodCategoryMenuComponent(
    foodCategoryExpanded: Boolean,
    foodCategory: FoodCategory,
    text: String,
    onTextChange: (String) -> Unit,
    onFoodCategoryExpandedChange: (Boolean) -> Unit,
    onFoodCategoryChange: (FoodCategory) -> Unit,
    onPostFoodClick: () -> Unit,
) {
    item {
        Box(
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .padding(8.dp)

        ) {
            Column {
                Text(
                    text = "먹은 음식 추가하기",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Yellow
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            onFoodCategoryExpandedChange(true)
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "(현재 먹은 음식 상태) : ${foodCategory.foodCategory}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Yellow
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "v",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Yellow
                        )
                    }
                    DropdownMenu(
                        expanded = foodCategoryExpanded,
                        onDismissRequest = {
                            onFoodCategoryExpandedChange(false)
                        },
                    ) {
                        FoodCategory.entries.forEach { foodCategoryOption ->
                            DropdownMenuItem(
                                text = { Text(text = foodCategoryOption.name) },
                                onClick = {
                                    onFoodCategoryChange(foodCategoryOption)
                                    onFoodCategoryExpandedChange(false)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "먹은 음식량[g] (예: 220)"
                        )
                    },
                    value = text,
                    onValueChange = {
                        onTextChange(it)
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.DarkGray,
                        focusedContainerColor = Color.DarkGray,
                        unfocusedPlaceholderColor = Color.Yellow,
                        unfocusedTextColor = Color.Yellow,
                        focusedTextColor = Color.Yellow,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPostFoodClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "추가하기", color = Color.Yellow)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        userInfo =
            UserInfoResponse(
                id = 0,
                createdAt = Date.from(Instant.now()),
                updateAt = Date.from(Instant.now()),
                name = "홍길동",
                email = "s12@gsm.hs.kr",
                phone = "010-1234-5678",
                weight = 58,
                height = 158,
                age = 34,
                gender = Gender.FEMALE,
                activityLevel = ActivityLevel.EXTREMELY_ACTIVE,
                mealFrequency = 3,
                stomachVolume = 30,
                token = UUID.randomUUID(),
            ),
        postEatFoodResultResponse = listOf(
            PostEatFoodResultResponse(
                userId = 0,
                type = FoodCategory.고기류,
                weight = 220,
                volume = 400F,
                ratio = 30F,
                createdAt = Date.from(Instant.now()),
                complete = Date.from(Instant.now()),
                foodId = UUID.randomUUID()
            ),
            PostEatFoodResultResponse(
                userId = 0,
                type = FoodCategory.과일류,
                weight = 220,
                volume = 400F,
                ratio = 30F,
                createdAt = Date.from(Instant.now()),
                complete = Date.from(Instant.now()),
                foodId = UUID.randomUUID()
            )
        ),
        onPostFoodClick = {},
        stomachInfoResponse =
            PostEatFoodResultResponse(
                userId = 0,
                type = FoodCategory.고기류,
                weight = 220,
                volume = 400F,
                ratio = 30F,
                createdAt = Date.from(Instant.now()),
                complete = Date.from(Instant.now()),
                foodId = UUID.randomUUID()
            ),
    )
}