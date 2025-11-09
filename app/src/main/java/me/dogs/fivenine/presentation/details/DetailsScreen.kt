package me.dogs.fivenine.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
fun DetailsScreen(viewModel: DetailsViewModel, onBack: () -> Unit) {
    val items by viewModel.items.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "> LIST_ITEMS_",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "> [+] new item",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clickable { showDialog.value = true }
                .padding(8.dp)
        )

        LazyColumn {
            items(items) { item ->
                Text(
                    text = "> ${item}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
    Text(
        text = "> [+] new item",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .clickable { /* TODO dialog */ }
            .padding(8.dp)
    )
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val data = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("> NEW ITEM", color = MaterialTheme.colorScheme.primary) },
        text = {
            TextField(
                value = data.value,
                onValueChange = { data.value = it },
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
                modifier = Modifier.clickable { onConfirm(data.value) }
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