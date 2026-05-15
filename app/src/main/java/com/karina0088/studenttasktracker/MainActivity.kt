package com.karina0088.studenttasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.karina0088.studenttasktracker.ui.AddEditScreen
import com.karina0088.studenttasktracker.ui.HomeScreen
import com.karina0088.studenttasktracker.ui.theme.StudentTaskTrackerTheme
import com.karina0088.studenttasktracker.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StudentTaskTrackerTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {

    val navController = rememberNavController()
    val viewModel: TaskViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController, viewModel)
        }

        composable("add") {
            AddEditScreen(navController, viewModel)
        }
    }
}