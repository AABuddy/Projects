package uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.screens.euclidean


import android.util.Range
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.Node
import uk.ac.aber.dcs.cs31620.demonstratingtsp.ui.theme.TSPTheme
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


private val action: MutableState<Any?> = mutableStateOf(null)
const val circleRadius = 60F

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EuclideanU(navController: NavController) {

    var finished = false
    var circleCoords: Offset
    var circles = mutableListOf<Node>()
    var count = 0
    val paths = mutableListOf<Node>()
    //var bestPath = mutableListOf<Node>()
    var generatedPath = false
    var bestPath = mutableListOf<Node>()

    Column(modifier = Modifier.fillMaxSize()) {

        Button(modifier = Modifier
            .height(100.dp)
            .width(300.dp),
            onClick = {
                finished = true
                if(paths.size == circles.size+1) {
                    generatedPath = true
                }
            }) {
            Text(text = "Confirm")
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            action.value = it
                            if (!finished) {
                                circleCoords = Offset(it.x, it.y)
                                val newCircle = Node(count, circleCoords.x, circleCoords.y)
                                circles = checkIfOverlap(newCircle, circles)
                                count++

                            } else {
                                println("Action Detected")
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

                                                println("Circle added")
                                            }
                                        }
                                    }
                                    if ((paths.size == circles.size) && (paths.containsAll(circles))) {
                                        paths.add(paths[0])
                                        println(paths)
                                        bestPath = testBrute(circles)
                                        true
                                    } else {
                                        false
                                    }
                                }
                            }
                        }

                    }
                    true
                }

        ) {
            for (circle in circles) {        //This is probably not the most efficient solution however currently
                drawCircle(                 //The best option I have for generating a GUI
                    color = Color.Black,
                    radius = circleRadius,
                    center = Offset(circle.x, circle.y),
                    style = Stroke(
                        width = 10f
                    )
                )
            }

            if (finished) {
                for (circle in circles) {
                    for (i in 0 until circles.size) {
                        if (circle.id != circles[i].id) {
                            drawLine(
                                start = Offset(circles[circle.id].x, circles[circle.id].y),
                                end = Offset(
                                    circles[i].x,
                                    circles[i].y
                                ),   //+1 Extends outside of the array
                                color = Color.Black.copy(alpha = 0.25f),
                                strokeWidth = 7.5f
                            )
                        }
                    }       //DRAWS LINES BETWEEN NODES
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
                        var start = Offset(paths[i - 1].x, paths[i - 1].y)
                        var end = Offset(paths[i].x, paths[i].y)
                        drawLine(
                            start = start,
                            end = end,   //+1 Extends outside of the array
                            color = Color.Red.copy(alpha = 0.25f),
                            strokeWidth = 7.5f
                        )
                    }
                    println(bestPath)
                    for (i in 1 until bestPath.size) {
                        var start = Offset(bestPath[i - 1].x, bestPath[i - 1].y)
                        var end = Offset(bestPath[i].x, bestPath[i].y)
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

fun checkIfOverlap(circle: Node, circles: MutableList<Node>): MutableList<Node> {

    if(circles.size >= 1) {
        var count = 0
        for(i in 0 until circles.size) {
            val dist = sqrt((circle.x.toDouble() - circles[i].x.toDouble()).pow(2.0) + (circle.y.toDouble() - circles[i].y.toDouble()).pow(2.0))

            if(dist < circleRadius * 2) {
                println("Circles overlapping")
            } else { count++ }

            if(count == circles.size) {
                circles.add(circle)
            }
        }
    } else {
        circles.add(circle)
    }
    return circles
}


fun testBrute(circles: MutableList<Node>): MutableList<Node> {
    var bestCost = 10000F
    var bestPath = listOf<Node>()
    val tempList = circles.toMutableList()
    val paths = permutations(tempList)
    for(i in paths.indices) {
        var currentCost = calcDistance(paths[i])
        if (currentCost < bestCost) {
            bestPath = paths[i]
            bestCost = currentCost
        }
    }
    val mlBestPath = bestPath.toMutableList()
    println(bestCost)
    println(bestPath)
    println(mlBestPath)
    return mlBestPath
}

fun permutations(input: MutableList<Node>): List<List<Node>> {
    val solutions = mutableListOf<List<Node>>()
    permutationsRecursive(input, 0, solutions)
    return solutions
}

fun permutationsRecursive(input: MutableList<Node>, index: Int, answers: MutableList<List<Node>>) {
    if (index == input.lastIndex){
        input.add(input[0])
        answers.add(input.toList())
        input.remove(input[input.size - 1])
    }
    for (i in index .. input.lastIndex) {
        Collections.swap(input, index, i)
        permutationsRecursive(input, index + 1, answers)
        Collections.swap(input, i, index)
    }
}

fun calcDistance(nodes: List<Node>): Float {
    var sum = 0F
    for( i in 0 until nodes.size - 1) {
        var dist = (sqrt((nodes[i].x.toDouble() - nodes[i+1].x.toDouble()).pow(2.0) + (nodes[i].y.toDouble() - nodes[i+1].y.toDouble()).pow(2.0))).toFloat()
        sum += dist
    }
    return sum
}








@Preview
@Composable
private fun EuclideanPreview() {
    TSPTheme(dynamicColor = false ) {
        //euclideanUser()
    }
}