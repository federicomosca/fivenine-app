package me.dogs.fivenine.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(viewModel: DetailsViewModel, onBack: () -> Unit) {
    val items by viewModel.items.collectAsState()
    val listType by viewModel.listType.collectAsState()
    val listName by viewModel.listName.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ListItem?>(null) }
    var itemToDelete by remember { mutableStateOf<ListItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "> $listName",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "> [+] new item",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clickable { showAddDialog = true }
                .padding(8.dp)
        )

        LazyColumn {
            items(items, key = { it.id }) { item ->
                ItemCard(
                    item = item,
                    onLongPress = { itemToEdit = item },
                    onDelete = { itemToDelete = item }
                )
            }
        }    }

    if (showAddDialog) {
        when (listType) {
            "movie" -> FilmDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, director, year, genre ->
                    viewModel.addFilm(title, director, year, genre)
                    showAddDialog = false
                }
            )
            "restaurant" -> RestaurantDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, address, type ->
                    viewModel.addRestaurant(name, address, type)
                    showAddDialog = false
                }
            )
            "book" -> BookDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, author, year, genre ->
                    viewModel.addBook(title, author, year, genre)
                    showAddDialog = false
                }
            )
            "videogame" -> VideoGameDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, year, publisher, platform, genre ->
                    viewModel.addVideoGame(title, year, publisher, platform, genre)
                    showAddDialog = false
                }
            )
            "custom" -> CustomItemDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { data ->
                    viewModel.addCustomItem(data)
                    showAddDialog = false
                }
            )
        }
    }

    itemToEdit?.let { item ->
        when (item) {
            is ListItem.Film -> FilmDialog(
                initialValues = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { title, director, year, genre ->
                    viewModel.updateItem(item.copy(title = title, director = director, year = year, genre = genre))
                    itemToEdit = null
                }
            )
            is ListItem.Restaurant -> RestaurantDialog(
                initialValues = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { name, address, type ->
                    viewModel.updateItem(item.copy(name = name, address = address, type = type))
                    itemToEdit = null
                }
            )
            is ListItem.Book -> BookDialog(
                initialValues = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { title, author, year, genre ->
                    viewModel.updateItem(item.copy(title = title, author = author, year = year, genre = genre))
                    itemToEdit = null
                }
            )
            is ListItem.VideoGame -> VideoGameDialog(
                initialValues = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { title, year, publisher, platform, genre ->
                    viewModel.updateItem(item.copy(title = title, year = year, publisher = publisher, platform = platform, genre = genre))
                    itemToEdit = null
                }
            )
            is ListItem.Custom -> CustomItemDialog(
                initialData = item.data,
                onDismiss = { itemToEdit = null },
                onConfirm = { data ->
                    viewModel.updateItem(item.copy(data = data))
                    itemToEdit = null
                }
            )
        }
    }

    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("> CONFIRM DELETE", color = MaterialTheme.colorScheme.primary) },
            text = { Text("Delete this item?", color = MaterialTheme.colorScheme.primary) },
            confirmButton = {
                Text(
                    text = "[DELETE]",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable {
                        viewModel.deleteItem(item)
                        itemToDelete = null
                    }
                )
            },
            dismissButton = {
                Text(
                    text = "[CANCEL]",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable { itemToDelete = null }
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    item: ListItem,
    onLongPress: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart || it == SwipeToDismissBoxValue.StartToEnd) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(16.dp)
            ) {
                Text(
                    text = "> [DELETE]",
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .combinedClickable(
                    onClick = {},
                    onLongClick = onLongPress
                )
                .padding(8.dp)
        ) {
            when (item) {
                is ListItem.Film -> {
                    Text(
                        text = "> ${item.title}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "  director: ${item.director} | year: ${item.year} | genre: ${item.genre}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                is ListItem.Restaurant -> {
                    Text(
                        text = "> ${item.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "  address: ${item.address} | type: ${item.type}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                is ListItem.Book -> {
                    Text(
                        text = "> ${item.title}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "  author: ${item.author} | year: ${item.year} | genre: ${item.genre}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                is ListItem.VideoGame -> {
                    Text(
                        text = "> ${item.title}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "  year: ${item.year} | publisher: ${item.publisher}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "  platform: ${item.platform} | genre: ${item.genre}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                is ListItem.Custom -> {
                    Text(
                        text = "> ${item.data}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun FilmDialog(
    initialValues: ListItem.Film? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf(initialValues?.title ?: "") }
    var director by remember { mutableStateOf(initialValues?.director ?: "") }
    var year by remember { mutableStateOf(initialValues?.year?.toString() ?: "") }
    var genre by remember { mutableStateOf(initialValues?.genre ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialValues == null) "> NEW FILM" else "> EDIT FILM", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("title") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = director,
                    onValueChange = { director = it },
                    label = { Text("director") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("year") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("genre") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            Text(
                text = "[OK]",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val yearInt = year.toIntOrNull() ?: 0
                    onConfirm(title, director, yearInt, genre)
                }
            )
        },
        dismissButton = {
            Text(
                text = "[CANCEL]",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    )
}

@Composable
fun RestaurantDialog(
    initialValues: ListItem.Restaurant? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialValues?.name ?: "") }
    var address by remember { mutableStateOf(initialValues?.address ?: "") }
    var type by remember { mutableStateOf(initialValues?.type ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialValues == null) "> NEW RESTAURANT" else "> EDIT RESTAURANT", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("name") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("address") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("type (chinese, italian, etc.)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            Text(
                text = "[OK]",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onConfirm(name, address, type) }
            )
        },
        dismissButton = {
            Text(
                text = "[CANCEL]",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    )
}

@Composable
fun BookDialog(
    initialValues: ListItem.Book? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf(initialValues?.title ?: "") }
    var author by remember { mutableStateOf(initialValues?.author ?: "") }
    var year by remember { mutableStateOf(initialValues?.year?.toString() ?: "") }
    var genre by remember { mutableStateOf(initialValues?.genre ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialValues == null) "> NEW BOOK" else "> EDIT BOOK", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("title") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("author") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("year") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("genre") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            Text(
                text = "[OK]",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val yearInt = year.toIntOrNull() ?: 0
                    onConfirm(title, author, yearInt, genre)
                }
            )
        },
        dismissButton = {
            Text(
                text = "[CANCEL]",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    )
}

@Composable
fun VideoGameDialog(
    initialValues: ListItem.VideoGame? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialValues?.title ?: "") }
    var year by remember { mutableStateOf(initialValues?.year?.toString() ?: "") }
    var publisher by remember { mutableStateOf(initialValues?.publisher ?: "") }
    var platform by remember { mutableStateOf(initialValues?.platform ?: "") }
    var genre by remember { mutableStateOf(initialValues?.genre ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialValues == null) "> NEW VIDEOGAME" else "> EDIT VIDEOGAME", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("title") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("year") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = publisher,
                    onValueChange = { publisher = it },
                    label = { Text("publisher") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = platform,
                    onValueChange = { platform = it },
                    label = { Text("platform (PS, Xbox, PC, etc.)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("genre") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            Text(
                text = "[OK]",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val yearInt = year.toIntOrNull() ?: 0
                    onConfirm(title, yearInt, publisher, platform, genre)
                }
            )
        },
        dismissButton = {
            Text(
                text = "[CANCEL]",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    )
}

@Composable
fun CustomItemDialog(
    initialData: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var data by remember { mutableStateOf(initialData) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialData.isEmpty()) "> NEW ITEM" else "> EDIT ITEM", color = MaterialTheme.colorScheme.primary) },
        text = {
            TextField(
                value = data,
                onValueChange = { data = it },
                label = { Text("data") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        confirmButton = {
            Text(
                text = "[OK]",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onConfirm(data) }
            )
        },
        dismissButton = {
            Text(
                text = "[CANCEL]",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    )
}