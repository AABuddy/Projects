package uk.ac.aber.dcs.cs31620.demonstratingtsp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLevelScaffold(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState? = null,
    snackbarContent: @Composable (SnackbarData) -> Unit = {},
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Scaffold(
        topBar = {
            MainPageTopAppBar()
        },
        content = { innerPadding ->
            pageContent(innerPadding)
        },
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    snackbarContent(data)
                }
            }
        }
    )
}