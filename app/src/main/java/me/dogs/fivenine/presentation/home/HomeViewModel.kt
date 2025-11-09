package me.dogs.fivenine.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dogs.fivenine.data.local.AppDatabase
import me.dogs.fivenine.data.model.ListEntity

class HomeViewModel(private val database: AppDatabase) : ViewModel() {
    val lists = database.listDao().getAllLists()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addList(name: String, type: String) {
        viewModelScope.launch {
            database.listDao().insertList(ListEntity(name = name, type = type))
        }
    }
}
