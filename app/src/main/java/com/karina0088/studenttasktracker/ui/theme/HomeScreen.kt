package com.karina0088.studenttasktracker.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    val isGrid by viewModel.isGrid.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val context = LocalContext.current

    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { viewModel.setIsGrid(!isGrid) }) {
                        Icon(
                            imageVector = if (isGrid) Icons.Default.List else Icons.Default.GridView,
                            contentDescription = "Toggle Layout"
                        )
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.setDarkMode(it) }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
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
                Text(stringResource(R.string.empty_state))
            }
        } else {
            if (isGrid) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks) { task ->
                        TaskCard(
                            task = task,
                            navController = navController,
                            onEditClick = { navController.navigate("edit/${task.id}") },
                            onDeleteClick = {
                                selectedTask = task
                                showDialog = true
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks) { task ->
                        TaskCard(
                            task = task,
                            navController = navController,
                            onEditClick = { navController.navigate("edit/${task.id}") },
                            onDeleteClick = {
                                selectedTask = task
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showDialog && selectedTask != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteTask(selectedTask!!)
                        Toast.makeText(context, context.getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show()
                        showDialog = false
                    }) {
                        Text(stringResource(R.string.delete_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                title = { Text(stringResource(R.string.delete_title)) },
                text = { Text(stringResource(R.string.delete_message)) }
            )
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    navController: NavController,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEditClick() }
            ) {
                Text(task.title, style = MaterialTheme.typography.titleSmall)
                Text(task.subject, style = MaterialTheme.typography.bodySmall)
                Text(task.deadline, style = MaterialTheme.typography.bodySmall)
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.menu_edit)) },
                        onClick = {
                            onEditClick()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.menu_delete)) },
                        onClick = {
                            onDeleteClick()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}