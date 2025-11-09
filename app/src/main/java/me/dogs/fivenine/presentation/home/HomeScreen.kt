package me.dogs.fivenine.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onListClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val lists by viewModel.lists.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

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
                .clickable { showDialog.value = true }
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(lists) { list ->
                Text(
                    text = "> ${list.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onListClick(list.id) }
                        .padding(8.dp)
                )
            }
        }
    }
    if (showDialog.value) {
        CreateListDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { name, type ->
                viewModel.addList(name, type)
                        showDialog.value = false
            }
        )
    }
}
@Composable
fun CreateListDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("movie") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("> NEW LIST", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("name") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("type: ${type.value}", color = MaterialTheme.colorScheme.primary)
                // TODO: dropdown per tipo
            }
        },
        confirmButton = {
            Text(
                text = "[OK]",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onConfirm(name.value, type.value) }
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