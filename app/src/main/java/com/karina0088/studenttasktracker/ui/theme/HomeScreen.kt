package com.karina0088.studenttasktracker.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.karina0088.studenttasktracker.R
import com.karina0088.studenttasktracker.data.Task
import com.karina0088.studenttasktracker.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {

    val tasks by viewModel.tasks.collectAsState()

    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Student Task Tracker")
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->

        if (tasks.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Belum ada tugas")
            }
        } else {

            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(tasks) { task ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(task.title)
                                Text(task.subject)
                                Text(task.deadline)
                            }

                            IconButton(onClick = {
                                selectedTask = task
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
        if (showDialog && selectedTask != null) {

            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },

                confirmButton = {
                    TextButton(onClick = {

                        viewModel.deleteTask(selectedTask!!)

                        Toast.makeText(
                            context,
                            "Task deleted",
                            Toast.LENGTH_SHORT
                        ).show()

                        showDialog = false
                    }) {
                        Text("Delete")
                    }
                },

                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancel")
                    }
                },

                title = {
                    Text("Delete Task")
                },

                text = {
                    Text("Are you sure?")
                }
            )
        }
    }
}