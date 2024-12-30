package com.qanti.myapplication.ui.compose.screen.signup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qanti.myapplication.data.model.enum.ActivityLevel
import com.qanti.myapplication.data.model.enum.Gender
import com.qanti.myapplication.data.request.SignUpRequest
import com.qanti.myapplication.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

const val ROUTE_SIGN_UP = "route_sign_up"
fun NavGraphBuilder.signUpScreen(
    onSignUpSuccess: () -> Unit
) {
    composable(ROUTE_SIGN_UP) {
        val userViewModel = hiltViewModel<UserViewModel>()
        val coroutineScope = rememberCoroutineScope()
        val signUpResponse = userViewModel.signUpResponse.collectAsStateWithLifecycle().value

        LaunchedEffect(Unit, signUpResponse) {
            if(userViewModel.checkLocalToken() != null) {
                onSignUpSuccess()
            } else {
                return@LaunchedEffect
            }
        }
        SignUpScreen(
            onSignUpButtonClick = { signUpRequest ->
                userViewModel.requestSignUp(body = signUpRequest)
                coroutineScope.launch {
                    if (userViewModel.checkLocalToken() != null) {
                        onSignUpSuccess()
                    } else {
                        return@launch
                    }
                }
            }
        )
    }
}

@Composable
private fun SignUpScreen(
    onSignUpButtonClick: (SignUpRequest) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var activityLevel by remember { mutableStateOf(ActivityLevel.SEDENTARY) }
    var mealFrequency by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        bottomBar = {
            Button(
                modifier = Modifier.padding(16.dp).fillMaxWidth().systemBarsPadding(),
                onClick = {
                    onSignUpButtonClick(
                        SignUpRequest(
                            name = name,
                            email = email,
                            phone = phone,
                            weight = weight.toInt(),
                            height = height.toInt(),
                            age = age.toInt(),
                            gender = gender,
                            activityLevel = activityLevel,
                            mealFrequency = mealFrequency.toInt(),
                        )
                    )
                }) {
                Text("회원가입")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                },
                placeholder = {
                    Text(
                        text = "이름을 입력해주세요. ex)이승제",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = {
                    Text(
                        text = "이메일을 입력해주세요. ex)abc@gmail.com",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = {
                    phone = it
                },
                placeholder = {
                    Text(
                        text = "전화번호를 입력해주세요. ex)010-1234-5678",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = weight,
                onValueChange = {
                    weight = it
                },
                placeholder = {
                    Text(
                        text = "몸무게를 입력해주세요(kg). ex)56",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = height,
                onValueChange = {
                    height = it
                },
                placeholder = {
                    Text(
                        text = "키를 입력해주세요.(cm) ex)150",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = age,
                onValueChange = {
                    age = it
                },
                placeholder = {
                    Text(
                        text = "나이를 입력해주세요. ex)16",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(15.dp))

            var genderExpanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier
                .background(Color.Green
                ).fillMaxWidth()
                .clickable { genderExpanded = true }) {
                Text(
                    text = "성별: ${gender.name}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                DropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false },
                ) {
                    Gender.entries.forEach { genderOption ->
                        DropdownMenuItem(
                            text = { Text(text = genderOption.name) },
                            onClick = {
                                gender = genderOption
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            var activityLevelExpanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .background(Color.Green)
                    .fillMaxWidth()
                    .clickable { activityLevelExpanded = true }
            ) {
                Text(
                    text = "활동 수준 선택: ${activityLevel.name}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                DropdownMenu(
                    expanded = activityLevelExpanded,
                    onDismissRequest = {
                        activityLevelExpanded = false
                    },
                ) {
                    ActivityLevel.entries.forEach { activityLevelOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = activityLevelOption.activityLevel)
                            },
                            onClick = {
                                activityLevel = activityLevelOption
                                activityLevelExpanded = false
                            }
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = mealFrequency,
                onValueChange = {
                    mealFrequency = it
                },
                placeholder = {
                    Text(
                        text = "하루 식사량을 입력해주세요. 최대 3 ex) (아침 점심 저녁을 먹는 경우) 3 ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(15.dp))

        }
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        onSignUpButtonClick = {}
    )
}