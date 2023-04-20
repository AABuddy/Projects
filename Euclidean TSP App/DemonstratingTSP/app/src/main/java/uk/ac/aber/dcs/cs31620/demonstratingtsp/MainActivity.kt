package uk.ac.aber.dcs.cs31620.demonstratingtsp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.DropDown
import uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.theme.TSPTheme
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.screens.euclidean.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TSPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                    //calcRoutes()
                }
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph() {
    // The NavController is in a place where all
    // our composables can access it.
    val navController = rememberNavController()

    // Each NavController is associated with a NavHost.
    // This links the NavController with a navigation graph.
    // As we navigate between composables the content of
    // the NavHost is automatically recomposed.
    // Each composable destination in the graph is associated with a route.
    NavHost(
        navController = navController,
        startDestination = Screen.Selection.route
    ) {
        composable(Screen.Selection.route) { SelectionScreen(navController) }
        composable(Screen.EuclideanR.route) { EuclideanR(navController) }
        composable(Screen.EuclideanU.route) { EuclideanU(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TSPTheme {
    }
}