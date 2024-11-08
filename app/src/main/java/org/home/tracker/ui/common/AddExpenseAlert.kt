package org.home.tracker.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseDto
import org.home.tracker.ui.expense.stringify

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    categories: List<CategoryDto>,
    expense: ExpenseDto? = null,
    submit: (ExpenseDto) -> Unit
) {

    val dayInMillis = 24 * 60 * 60 * 1000

    var showDatePicker by remember { mutableStateOf(false) }
    var editCategory by remember { mutableStateOf(false) }
    val selectedDate = rememberDatePickerState(
        initialSelectedDateMillis = expense?.date ?: System.currentTimeMillis()
    )

    val currencies = listOf("KGS", "RUB", "USD")

    var selectedCurrency by remember { mutableStateOf(expense?.currencyId ?: currencies[0]) }

    var selectedCategory by remember {
        if (categories.isEmpty()) mutableStateOf("")
        else
            mutableStateOf(expense?.category?.name ?: categories[0].name)
    }

    var value by remember {
        if (expense?.value != null) {
            mutableStateOf((expense.value / 100f).toString())
        } else {
            mutableStateOf("")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { /*TODO*/ }) {
            DatePicker(state = selectedDate)
        }
    }

    if (editCategory) {
        AddNewDialog(
            title = "Add new category",
            exists = categories.map { it.name }.toSet(),
            onConfirm = {
                selectedCategory = it
                editCategory = false
            },
            onDismiss = { editCategory = false })
    }

    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                selectedDate.setSelection(
                    (selectedDate.selectedDateMillis ?: 0L) - dayInMillis
                )
            }
        ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = stringify(selectedDate.selectedDateMillis ?: 0L),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .weight(3f)
                .clickable { showDatePicker = true }
        )
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                selectedDate.setSelection(
                    (selectedDate.selectedDateMillis ?: 0L) + dayInMillis
                )
            }
        ) {
            Icon(Icons.Rounded.ArrowForward, contentDescription = "Forward")
        }
    }
    Row(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
    ) {
        DropdownMenu(
            values = categories,
            extract = { it.name },
            allowAdd = true,
            selectedValue = categories.find { it.name == selectedCategory },
            onValueChange = { selectedCategory = it }
        )
    }
    Row(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = value,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = { value = it },
            placeholder = { Text(text = "0") },
            modifier = Modifier.weight(4f)
        )

        DropdownMenu(
            values = currencies,
            extract = { it },
            onValueChange = { selectedCurrency = it },
            modifier = Modifier.weight(3f)
        )
    }

    Row {
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val category = categories.find { it.name == selectedCategory }
                    val id = expense?.id ?: 0L
                    val expenseDto = ExpenseDto(
                        id = id,
                        date = selectedDate.selectedDateMillis ?: 0L,
                        value = (value.replace(",", ".").toDouble() * 100).toLong(),
                        category = category ?: CategoryDto(null, selectedCategory),
                        currencyId = selectedCurrency
                    )

                    submit(expenseDto)
                },
                modifier = Modifier
                    .width(150.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Sharp.Check, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddExpenseDialogPreview() {
    val categories = listOf(
        CategoryDto(1L, "Goods"),
        CategoryDto(2L, "Travel")
    )
    MaterialTheme {
        Column {
            AddExpenseDialog(categories) {}
        }
    }
}