package me.dogs.fivenine.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dogs.fivenine.data.local.AppDatabase
import me.dogs.fivenine.data.model.ListItemEntity

class DetailsViewModel(private val database: AppDatabase, private val listId: Int) : ViewModel() {
    val items = database.listItemDao().getItemsByListId(listId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addItem(data: String) {
        viewModelScope.launch {
            database.listItemDao().insertItem(
                ListItemEntity(listId = listId, data = data)
            )
        }
    }
}
