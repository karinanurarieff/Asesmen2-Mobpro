package com.karina0088.studenttasktracker.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.karina0088.studenttasktracker.data.Task
import com.karina0088.studenttasktracker.viewmodel.TaskViewModel
import androidx.compose.ui.res.stringResource
import com.karina0088.studenttasktracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    navController: NavController,
    viewModel: TaskViewModel,
    existingTask: Task? = null  // null = mode add, ada isi = mode edit
) {

    val isEditMode = existingTask != null

    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var subject by remember { mutableStateOf(existingTask?.subject ?: "") }
    var deadline by remember { mutableStateOf(existingTask?.deadline ?: "") }
    var isDone by remember { mutableStateOf(existingTask?.isDone ?: false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title =
                    { Text(stringResource(if (isEditMode) R.string.edit_task else R.string.add_task))
                    },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
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
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.task_title)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text(stringResource(R.string.subject)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (deadline.isEmpty()) stringResource(R.string.pick_deadline) else deadline)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { isDone = it }
                )
                Text(stringResource(R.string.mark_as_done))

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isEmpty() || subject.isEmpty() || deadline.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show()
                    } else {
                        if (isEditMode) {
                            viewModel.updateTask(
                                existingTask!!.copy(
                                    title = title,
                                    subject = subject,
                                    deadline = deadline,
                                    isDone = isDone
                                )
                            )
                            Toast.makeText(context, context.getString(R.string.toast_updated), Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addTask(
                                Task(
                                    title = title,
                                    subject = subject,
                                    deadline = deadline,
                                    isDone = isDone
                                )
                            )
                            Toast.makeText(context, context.getString(R.string.toast_saved), Toast.LENGTH_SHORT).show()
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(if (isEditMode) R.string.update else R.string.save))
            }
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            deadline = sdf.format(java.util.Date(millis))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}