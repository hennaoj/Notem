package com.example.notem.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NotemAppState(
    val navController: NavHostController
) {
    fun navigateBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberNotemAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    NotemAppState(navController)
}