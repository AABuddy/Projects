package uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.screens.euclidean


import android.util.Range
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.DefaultSnackbar
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.Node
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.demonstratingtsp.solvers.HeldKarpy
//import uk.ac.aber.dcs.cs31620.demonstratingtsp.solvers.HeldKarpAlgorithm
//import uk.ac.aber.dcs.cs31620.demonstratingtsp.solvers.HeldKarp
import uk.ac.aber.dcs.cs31620.demonstratingtsp.solvers.testBrute
import uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.theme.TSPTheme
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

var bestPath = mutableListOf<Node>()
private val action: MutableState<Any?> = mutableStateOf(null)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EuclideanR(navController: NavHostController) {

    val config = LocalConfiguration.current
    val circleRadius = 40F
    val screenWidth = (config.screenWidthDp)
    val screenHeight = (config.screenHeightDp)

    val circles = mutableListOf<Node>()
    val paths = mutableListOf<Node>()
    var id = 0
    var generatedPath = false
    val snackbarHostState = remember { SnackbarHostState() }



    while(circles.size < 5) {
        val random = Random(System.currentTimeMillis())
        val xCoord =
            random.nextInt(circleRadius.toInt(), (screenWidth - circleRadius).toInt()).toFloat()
        val yCoord =
            random.nextInt(circleRadius.toInt(), (screenHeight - circleRadius).toInt()).toFloat()
        val newCircle = Node(id, xCoord, yCoord)

        if(id >= 1) {
            var count = 0
            for(i in 0 until circles.size) {
                val dist = (sqrt((newCircle.x.toDouble() - circles[i].x.toDouble()).pow(2.0) + (newCircle.y.toDouble() - circles[i].y.toDouble()).pow(2.0))).toFloat()
                if(dist >= circleRadius * 2) {
                    count++
                }
                if(count == circles.size) {
                    circles.add(newCircle)
                    id++
                }
            }
        } else {
            circles.add(newCircle)
            id++
        }
    }

    if(algorithmSelection == "Brute Force") { bestPath = testBrute(circles) }
    else if (algorithmSelection == "Greedy") {bestPath = testBrute(circles)}
    else{
        println("Algorithm has not been implemented yet")
    }

    //val heldKarpy = HeldKarpy(circles)
    //val tempPath = heldKarpy.shortPath().second


    //for(l in tempPath.indices) {
     //  bestPath.add(circles[tempPath[l]])
    //}



TopLevelScaffold(navController = navController,
    snackbarContent = { data ->
    DefaultSnackbar(
        data = data,
        modifier = Modifier.padding(bottom = 4.dp),
        onDismiss = {
            // An opportunity to do work such as undoing the
            // add cat operation. We'll just dismiss the snackbar
            data.dismiss()
        }
    )
},
    snackbarHostState = snackbarHostState
) { innerPadding ->

    Column(modifier = Modifier.fillMaxSize()
        .padding(innerPadding)) {
        var (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }

        Button(modifier = Modifier
            .height(100.dp)
            .width(300.dp),
            onClick = {
                if (paths.size == circles.size + 1) {
                    generatedPath = true
                }
            }) {
            Text(text = "Confirm")
        }

        val scope = rememberCoroutineScope()

        if (snackbarVisibleState) {
            Snackbar( modifier = Modifier.padding(8.dp)) {
                Text(text = calcDistance(circles).toString())
            } }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        action.value = it
                        if (!generatedPath) {
                            for (circle in circles) {
                                if (it.x in Range(
                                        circle.x - circleRadius,
                                          circle.x + circleRadius
                                    ) &&
                                    (it.y in Range(
                                        circle.y - circleRadius,
                                        circle.y + circleRadius
                                    ))
                                ) {
                                    if (paths.contains(circle)) {
                                        paths.remove(circle)
                                        if (paths.size == circles.size) {
                                            paths.removeAt(paths.lastIndex)
                                        }

                                    } else {
                                        paths.add(circle)
                                        scope.launch {
                                            snackbarHostState.showSnackbar(calcDistance(paths).toString())
                                        }
                                    }
                                }
                            }
                            if ((paths.size == circles.size) && (paths.containsAll(circles))) {
                                paths.add(paths[0])
                            }
                        }
                    }
                }
                true
            })
        {
            for (circle in circles) {
                drawCircle(
                    color = Color.Black,
                    radius = circleRadius,
                    center = Offset(circle.x, circle.y),
                    style = Stroke(
                        width = 10f
                    )
                )
            }
            //TESTING CODE FOR PATH GENERATION
            for (circle in circles) {
                for (i in 0 until circles.size) {
                    if (circle.id != circles[i].id) {        //For every other circle
                        val start = Offset(circles[circle.id].x, circles[circle.id].y)
                        val end = Offset(circles[i].x, circles[i].y)
                        drawLine(
                            start = start,
                            end = end,   //+1 Extends outside of the array
                            color = Color.Black.copy(alpha = 0.25f),    //ALPHA ALTERS THE LINES OPACITY
                            strokeWidth = 7.5f
                        )
                    }
                }
            }

            action.value.let {
                for (circle in circles) {
                    drawCircle(
                        color = Color.Black,
                        radius = circleRadius,
                        center = Offset(circle.x, circle.y),
                        style = Stroke(
                            width = 10f
                        )
                    )
                }
                if(!generatedPath) {
                    for (circle in paths) {
                        drawCircle(
                            color = Color.Blue,
                            radius = circleRadius,
                            center = Offset(circle.x, circle.y),
                            style = Stroke(
                                width = 10f
                            )
                        )
                    }
                }

                if (generatedPath) {
                    for (i in 1 until paths.size) {
                        val start = Offset(paths[i - 1].x, paths[i - 1].y)
                        val end = Offset(paths[i].x, paths[i].y)
                        drawLine(
                            start = start,
                            end = end,   //+1 Extends outside of the array
                            color = Color.Red.copy(alpha = 0.25f),
                            strokeWidth = 7.5f
                        )
                    }

                    for (i in 1 until bestPath.size) {
                        val start = Offset(bestPath[i - 1].x, bestPath[i - 1].y)
                        val end = Offset(bestPath[i].x, bestPath[i].y)
                        drawLine(
                            start = start,
                            end = end,   //+1 Extends outside of the array
                            color = Color.Green.copy(alpha = 0.5f),
                            strokeWidth = 7.5f
                        )
                    }
                }
                action.value = null
            }
        }

    }
}
}



@Preview
@Composable
private fun MainScreenPreview() {
    val navController = rememberNavController()
    TSPTheme(dynamicColor = false) {
        //euclideanR(navController)
    }
}
