package org.home.tracker.ui.expense.rule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseRuleDto
import org.home.tracker.ui.common.DropdownMenu

@Composable
fun ExpenseRuleItem(
    rule: ExpenseRuleDto?,
    categories: List<CategoryDto>,
    onSave: (ExpenseRuleDto) -> Unit,
    onDelete: (Long?) -> Unit
) {

    var containsFieldState by remember { mutableStateOf(rule?.pattern ?: "") }
    var selectedCategory by remember { mutableStateOf(rule?.category) }

    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row {
            TextField(
                value = containsFieldState,
                onValueChange = { containsFieldState = it },
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Forward",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            DropdownMenu(
                values = categories,
                extract = { it.name },
                allowAdd = false,
                selectedValue = selectedCategory,
                onValueChange = { value -> selectedCategory = categories.find { it.name == value } },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            if (containsFieldState != rule?.pattern || selectedCategory != rule.category) {
                Button(onClick = {
                    val dto = ExpenseRuleDto(
                        id = rule?.id ?: 0L,
                        pattern = containsFieldState,
                        category = selectedCategory!!
                    )
                    onSave(dto)
                }) { Text("Save") }
            }
            Button(onClick = { onDelete(rule?.id) }) { Text("Delete") }
        }
    }
}

@Preview
@Composable
private fun ExpenseRuleItemPreview() {
    val categories = listOf(
        CategoryDto(1L, "Goods"),
        CategoryDto(2L, "Restaurants")
    )

    val rule = ExpenseRuleDto(
        1L,
        "MARKET",
        categories[1]
    )

    ExpenseRuleItem(rule, categories, {}, {})
}