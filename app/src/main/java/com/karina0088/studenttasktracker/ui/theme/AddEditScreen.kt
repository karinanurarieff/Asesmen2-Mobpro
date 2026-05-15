package com.karina0088.studenttasktracker.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.karina0088.studenttasktracker.data.Task
import com.karina0088.studenttasktracker.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {

    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Task")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = {
                    Text("Task Title")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = subject,
                onValueChange = {
                    subject = it
                },
                label = {
                    Text("Subject")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = deadline,
                onValueChange = {
                    deadline = it
                },
                label = {
                    Text("Deadline")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                if (
                    title.isEmpty() ||
                    subject.isEmpty() ||
                    deadline.isEmpty()
                ) {

                    Toast.makeText(
                        context,
                        "All fields must be filled",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    viewModel.addTask(
                        Task(
                            title = title,
                            subject = subject,
                            deadline = deadline
                        )
                    )

                    Toast.makeText(
                        context,
                        "Task saved",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.popBackStack()
                }
            }) {
                Text("Save")
            }
        }
    }
}