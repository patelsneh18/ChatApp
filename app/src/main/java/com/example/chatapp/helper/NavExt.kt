package com.example.chatapp.helper

import androidx.navigation.NavController
import com.example.chatapp.ui.Screen

fun NavController.navigateTo(
    to: Screen,
    popUpTo: Screen? = null,
    inclusive: Boolean = true
) {
    if (popUpTo!=null) popBackStack(popUpTo.route, inclusive)
    navigate(to.route)
}

fun NavController.navigateTo(
    to: String,
    popUpTo: String? = null,
    inclusive: Boolean = true
) {
    if (popUpTo!=null) popBackStack(popUpTo, inclusive)
    navigate(to)
}