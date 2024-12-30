package com.qanti.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.qanti.myapplication.ui.compose.screen.main.ROUTE_MAIN
import com.qanti.myapplication.ui.compose.screen.main.mainScreen
import com.qanti.myapplication.ui.compose.screen.signup.ROUTE_SIGN_UP
import com.qanti.myapplication.ui.compose.screen.signup.signUpScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QantiStomachNavHost()
        }
    }
}
@Composable
fun QantiStomachNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ROUTE_SIGN_UP,
    ) {
        signUpScreen(
            onSignUpSuccess = { navController.navigate(ROUTE_MAIN) }
        )
        mainScreen(

        )
    }
}