package org.home.tracker.ui.expense.rule

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.home.tracker.AppViewModelProvider
import org.home.tracker.ui.expense.rule.viewmodel.ExpenseRuleViewModel

@Composable
fun ExpenseRulesScreen(
    viewModel: ExpenseRuleViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val items by remember { viewModel.state }
    val categories by remember { viewModel.categoryState }

    Scaffold { paddings ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            modifier = Modifier
                .padding(top = 50.dp)
                .padding(paddings)
                .fillMaxHeight()
        ) {
            items(items) {
                ExpenseRuleItem(
                    rule = it,
                    categories,
                    onSave = { rule -> viewModel.save(rule) },
                    onDelete = { ruleId -> viewModel.delete(ruleId) }
                )
            }
            item {
                Button(onClick = { viewModel.addEmpty() }) {
                    Text("Add rule")
                }
            }
        }
    }

}

@Composable
@Preview
private fun ExpenseRulesScreenPreview() {
    ExpenseRulesScreen()
}