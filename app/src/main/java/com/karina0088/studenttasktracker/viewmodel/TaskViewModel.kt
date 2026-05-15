package com.karina0088.studenttasktracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.karina0088.studenttasktracker.data.Task
import com.karina0088.studenttasktracker.data.TaskDatabase
import com.karina0088.studenttasktracker.preferences.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TaskDatabase
        .getDatabase(application)
        .taskDao()

    private val themePreferences = ThemePreferences(application)

    val tasks = dao.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val isDarkMode = themePreferences.darkModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            themePreferences.saveDarkMode(isDark)
        }
    }

    val isGrid = themePreferences.isGridFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun setIsGrid(isGrid: Boolean) {
        viewModelScope.launch {
            themePreferences.saveIsGrid(isGrid)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            dao.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            dao.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.deleteTask(task)
        }
    }
}