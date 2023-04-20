package uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.navigation

sealed class Screen(val route: String) {
    object Selection : Screen("selection")
    object EuclideanR : Screen("euclideanR")
    object EuclideanU : Screen("euclideanU")
}

/**
 * List of top-lvel screens provided as a convenience.
 */
val screens = listOf(
    Screen.Selection,
    Screen.EuclideanR,
    Screen.EuclideanU
)