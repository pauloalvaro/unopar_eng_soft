package com.manus.lara.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.manus.lara.data.local.LaraDatabase
import com.manus.lara.data.local.SecurePrefsManager
import com.manus.lara.data.local.TaskEntity
import com.manus.lara.data.repository.LaraRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class LaraViewModel(application: Application) : AndroidViewModel(application) {
    private val securePrefs = SecurePrefsManager(application)
    private val database = LaraDatabase.getDatabase(application)
    private val laraDao = database.laraDao()
    
    private var repository: LaraRepository? = null

    init {
        securePrefs.getApiKey()?.let {
            repository = LaraRepository(laraDao, it)
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val today: Long
        get() = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    val todayTasks: Flow<List<TaskEntity>> = repository?.getTasksForToday(today) ?: flowOf(emptyList())
    val completedTasks: Flow<List<TaskEntity>> = repository?.getCompletedTasks() ?: flowOf(emptyList())

    fun saveApiKey(apiKey: String) {
        securePrefs.saveApiKey(apiKey)
        repository = LaraRepository(laraDao, apiKey)
    }

    fun completeOnboarding(rooms: String, hasKids: Boolean, hasPets: Boolean, appliances: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository?.generateInitialRoutine(rooms, hasKids, hasPets, appliances)
            securePrefs.setOnboardingComplete(true)
            _isLoading.value = false
        }
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch {
            repository?.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun handleUnforeseen(event: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository?.handleUnforeseen(event)
            _isLoading.value = false
        }
    }

    fun isConfigured() = securePrefs.getApiKey() != null
    fun isOnboardingComplete() = securePrefs.isOnboardingComplete()
}
