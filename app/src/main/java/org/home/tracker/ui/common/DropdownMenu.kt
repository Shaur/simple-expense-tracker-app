package org.home.tracker.ui.common

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> DropdownMenu(
    values: List<T>,
    modifier: Modifier = Modifier,
    selectedValue: T? = null,
    allowAdd: Boolean = false,
    extract: (T) -> String,
    onValueChange: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var showEditor by remember { mutableStateOf(false) }
    var selected by remember {
        val value = if (selectedValue != null) {
            extract(selectedValue)
        } else if (values.isNotEmpty()) {
            extract(values.first())
        } else {
            ""
        }

        mutableStateOf(value)
    }

    if (showEditor) {
        AddNewDialog(
            title = "Add new",
            exists = values.map { extract(it) }.toSet(),
            onConfirm = {
                selected = it
                showEditor = false
                onValueChange(it)
            },
            onDismiss = { showEditor = false })
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = selected,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selected = extract(selectionOption)
                        onValueChange(selected)
                        expanded = false
                    }
                ) {
                    Text(text = extract(selectionOption))
                }
            }

            if (allowAdd) {
                DropdownMenuItem(
                    onClick = {
                        showEditor = true
                        expanded = false
                    }
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add new category")
                    Text(text = "Add new")
                }
            }
        }
    }


}