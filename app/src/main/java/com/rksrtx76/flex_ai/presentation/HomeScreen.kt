package com.rksrtx76.flex_ai.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.rksrtx76.flex_ai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val scrollState = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

//    Scaffold(
//        topBar = {
//            AppBar(title = stringResource(R.string.app_name), scrollState)
//        },
//        modifier = Modifier.nestedScroll(scrollState.nestedScrollConnection)
//    ){
        ChatScreen()
//    }
}