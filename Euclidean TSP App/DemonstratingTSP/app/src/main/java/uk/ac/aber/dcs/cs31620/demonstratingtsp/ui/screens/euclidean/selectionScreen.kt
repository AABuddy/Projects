package uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.screens.euclidean


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.DropDown

var algorithmSelection = ""

@Composable
fun SelectionScreen(navController: NavController) {
    var generationSelection = ""

    val items = mutableListOf("Random Euclidean", "User Euclidean")
    val algorithms = mutableListOf("Brute Force", "Greedy", "Held-Karp (TODO)")

    Box {
        Column(modifier = Modifier.fillMaxSize()) {

            Row( horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)) {
                DropDown(modifier = Modifier
                    .align(Alignment.Top)
                    .width(250.dp)
                    .height(50.dp),
                    items = items,
                    itemClick = {
                        generationSelection = it
                    },
                )

                Button(onClick = {
                    if (generationSelection == "Random Euclidean") {
                        navController.navigate("euclideanR")
                    }
                    if (generationSelection == "User Euclidean") {
                        navController.navigate("euclideanU")
                    }
                },
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(horizontal = 20.dp)
                        .width(250.dp)
                        .height(50.dp)) {}
            }

            Row( horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)) {
                DropDown(modifier = Modifier
                    .align(Alignment.Top)
                    .width(250.dp)
                    .height(50.dp),
                    items = algorithms,
                    itemClick = {
                        algorithmSelection = it
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun prevSelect() {
    val navController = rememberNavController()
    SelectionScreen(navController)
}