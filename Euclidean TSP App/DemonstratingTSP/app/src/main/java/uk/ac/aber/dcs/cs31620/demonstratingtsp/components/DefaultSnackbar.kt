package uk.ac.aber.dcs.cs31620.demonstratingtsp.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    Snackbar(
        modifier = modifier,
        content = {
            Text(
                text = data.visuals.message
            )
        },
        action = {
            data.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(
                        text = actionLabel
                    )
                }
            }
        },

    )
}