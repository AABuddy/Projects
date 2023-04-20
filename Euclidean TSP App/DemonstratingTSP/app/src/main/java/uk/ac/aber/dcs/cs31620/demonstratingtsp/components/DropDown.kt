package uk.ac.aber.dcs.cs31620.demonstratingtsp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropDown(
    items: List<String>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    itemClick: (String) -> Unit = {},
) {

    var itemText by rememberSaveable { mutableStateOf("Select Option") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    OutlinedButton(
        modifier = modifier,
        onClick = { expanded = !expanded }
    ) {
        Text(
            text = itemText,
            fontSize = fontSize,
            modifier = Modifier.padding(end = 8.dp)
        )

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        )
        {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        //Collapse the dropdown
                        expanded = false

                        // Remember the name of the item selected
                        itemText = it

                        // Hoist the state to the caller
                        itemClick(it)


                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun preview() {
    val items = listOf("1", "2", "3")
    DropDown(items = items)
}
