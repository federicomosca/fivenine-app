package me.dogs.fivenine.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.dogs.fivenine.data.model.ListEntity

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onListClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val lists by viewModel.lists.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<ListEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ){
        Text(
            text = "> FIVENINE_",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "> [+] new list",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clickable { showDialog = true }
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(lists, key = { it.id }) { list ->
                ListCard(
                    list = list,
                    onClick = { onListClick(list.id) },
                    onLongPress = { listToDelete = list },
                    onDelete = { listToDelete = list },
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }

    if (showDialog) {
        CreateListDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, type ->
                viewModel.addList(name, type)
                showDialog = false
            }
        )
    }

    // Delete confirmation dialog
    listToDelete?.let { list ->
        AlertDialog(
            onDismissRequest = { listToDelete = null },
            title = { Text("> CONFIRM DELETE", color = MaterialTheme.colorScheme.primary) },
            text = { Text("Delete list '${list.name}' and all its items?", color = MaterialTheme.colorScheme.primary) },
            confirmButton = {
                Text(
                    text = "[DELETE]",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable {
                        viewModel.deleteList(list)
                        listToDelete = null
                    }
                )
            },
            dismissButton = {
                Text(
                    text = "[CANCEL]",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable { listToDelete = null }
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListCard(
    list: ListEntity,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
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
        },
        dismissContent = {
            Text(
                text = "> ${list.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongPress
                    )
                    .padding(8.dp)
            )
        }
    )
}

@Composable
fun CreateListDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("movie") }
    var expanded by remember { mutableStateOf(false) }

    val listTypes = listOf(
        "movie" to "Movies",
        "restaurant" to "Restaurants",
        "book" to "Books",
        "videogame" to "Videogames",
        "custom" to "Custom"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("> NEW LIST", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("name") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Type dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = listTypes.find { it.first == selectedType }?.second ?: "Movies",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("type") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listTypes.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = MaterialTheme.colorScheme.primary) },
                                onClick = {
                                    selectedType = value
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = "[OK]",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onConfirm(name, selectedType) }
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
