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

    fun deleteList(list: ListEntity) {
        viewModelScope.launch {
            // Delete the list itself
            database.listDao().deleteList(list)

            // Delete all items associated with the list based on its type
            when (list.type) {
                "movie" -> database.filmDao().deleteFilmsByListId(list.id)
                "restaurant" -> database.restaurantDao().deleteRestaurantsByListId(list.id)
                "book" -> database.bookDao().deleteBooksByListId(list.id)
                "videogame" -> database.videoGameDao().deleteVideoGamesByListId(list.id)
                "custom" -> database.customItemDao().deleteCustomItemsByListId(list.id)
            }
        }
    }
}
