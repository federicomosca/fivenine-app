package me.dogs.fivenine.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.dogs.fivenine.data.local.AppDatabase
import me.dogs.fivenine.data.model.*

// Sealed interface to represent different item types
sealed interface ListItem {
    val id: Int
    val listId: Int

    data class Film(
        override val id: Int,
        override val listId: Int,
        val title: String,
        val director: String,
        val year: Int,
        val genre: String
    ) : ListItem {
        fun toEntity() = FilmEntity(id, listId, title, director, year, genre)
    }

    data class Restaurant(
        override val id: Int,
        override val listId: Int,
        val name: String,
        val address: String,
        val type: String
    ) : ListItem {
        fun toEntity() = RestaurantEntity(id, listId, name, address, type)
    }

    data class Book(
        override val id: Int,
        override val listId: Int,
        val title: String,
        val author: String,
        val year: Int,
        val genre: String
    ) : ListItem {
        fun toEntity() = BookEntity(id, listId, title, author, year, genre)
    }

    data class VideoGame(
        override val id: Int,
        override val listId: Int,
        val title: String,
        val year: Int,
        val publisher: String,
        val platform: String,
        val genre: String
    ) : ListItem {
        fun toEntity() = VideoGameEntity(id, listId, title, year, publisher, platform, genre)
    }

    data class Custom(
        override val id: Int,
        override val listId: Int,
        val data: String
    ) : ListItem {
        fun toEntity() = CustomItemEntity(id, listId, data)
    }
}

class DetailsViewModel(
    private val database: AppDatabase,
    private val listId: Int
) : ViewModel() {

    // Get list info to determine type
    private val _listInfo = MutableStateFlow<ListEntity?>(null)
    val listType: StateFlow<String> = _listInfo
        .map { it?.type ?: "movie" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "movie")

    val listName: StateFlow<String> = _listInfo
        .map { it?.name ?: "" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    // Combined items flow based on list type
    val items: StateFlow<List<ListItem>> = listType.flatMapLatest { type ->
        when (type) {
            "movie" -> database.filmDao().getFilmsByListId(listId).map { films ->
                films.map { ListItem.Film(it.id, it.listId, it.title, it.director, it.year, it.genre) }
            }
            "restaurant" -> database.restaurantDao().getRestaurantsByListId(listId).map { restaurants ->
                restaurants.map { ListItem.Restaurant(it.id, it.listId, it.name, it.address, it.type) }
            }
            "book" -> database.bookDao().getBooksByListId(listId).map { books ->
                books.map { ListItem.Book(it.id, it.listId, it.title, it.author, it.year, it.genre) }
            }
            "videogame" -> database.videoGameDao().getVideoGamesByListId(listId).map { games ->
                games.map { ListItem.VideoGame(it.id, it.listId, it.title, it.year, it.publisher, it.platform, it.genre) }
            }
            "custom" -> database.customItemDao().getCustomItemsByListId(listId).map { items ->
                items.map { ListItem.Custom(it.id, it.listId, it.data) }
            }
            else -> flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadListInfo()
    }

    private fun loadListInfo() {
        viewModelScope.launch {
            database.listDao().getAllLists().collect { lists ->
                _listInfo.value = lists.find { it.id == listId }
            }
        }
    }

    fun addFilm(title: String, director: String, year: Int, genre: String) {
        viewModelScope.launch {
            database.filmDao().insertFilm(
                FilmEntity(listId = listId, title = title, director = director, year = year, genre = genre)
            )
        }
    }

    fun addRestaurant(name: String, address: String, type: String) {
        viewModelScope.launch {
            database.restaurantDao().insertRestaurant(
                RestaurantEntity(listId = listId, name = name, address = address, type = type)
            )
        }
    }

    fun addBook(title: String, author: String, year: Int, genre: String) {
        viewModelScope.launch {
            database.bookDao().insertBook(
                BookEntity(listId = listId, title = title, author = author, year = year, genre = genre)
            )
        }
    }

    fun addVideoGame(title: String, year: Int, publisher: String, platform: String, genre: String) {
        viewModelScope.launch {
            database.videoGameDao().insertVideoGame(
                VideoGameEntity(listId = listId, title = title, year = year, publisher = publisher, platform = platform, genre = genre)
            )
        }
    }

    fun addCustomItem(data: String) {
        viewModelScope.launch {
            database.customItemDao().insertCustomItem(
                CustomItemEntity(listId = listId, data = data)
            )
        }
    }

    fun updateItem(item: ListItem) {
        viewModelScope.launch {
            when (item) {
                is ListItem.Film -> database.filmDao().updateFilm(item.toEntity())
                is ListItem.Restaurant -> database.restaurantDao().updateRestaurant(item.toEntity())
                is ListItem.Book -> database.bookDao().updateBook(item.toEntity())
                is ListItem.VideoGame -> database.videoGameDao().updateVideoGame(item.toEntity())
                is ListItem.Custom -> database.customItemDao().updateCustomItem(item.toEntity())
            }
        }
    }

    fun deleteItem(item: ListItem) {
        viewModelScope.launch {
            when (item) {
                is ListItem.Film -> database.filmDao().deleteFilm(item.toEntity())
                is ListItem.Restaurant -> database.restaurantDao().deleteRestaurant(item.toEntity())
                is ListItem.Book -> database.bookDao().deleteBook(item.toEntity())
                is ListItem.VideoGame -> database.videoGameDao().deleteVideoGame(item.toEntity())
                is ListItem.Custom -> database.customItemDao().deleteCustomItem(item.toEntity())
            }
        }
    }
}
