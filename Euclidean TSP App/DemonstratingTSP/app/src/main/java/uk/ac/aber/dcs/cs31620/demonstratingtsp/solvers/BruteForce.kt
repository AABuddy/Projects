package uk.ac.aber.dcs.cs31620.demonstratingtsp.solvers

import androidx.compose.runtime.Composable
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.Node
import kotlin.math.pow
import kotlin.math.sqrt
import java.util.Collections

/*@Composable
fun calcRoutes(circles: MutableList<Node>): Int {      //Works calcing routes, Routes = !(n-1)
    val n = circles.size - 1
    var routes = 1
    for(i in 1 .. n) { routes *= i }
    return routes
} */


@Composable
fun testBrute(circles: MutableList<Node>): MutableList<Node> {
    var bestCost = 10000F
    var bestPath = listOf<Node>()
    val tempList = circles.toMutableList()
    val paths = permutations(tempList)
    for (i in paths.indices) {
        val currentCost = calcDistance(paths[i])
        if (currentCost < bestCost) {
            bestPath = paths[i]
            bestCost = currentCost
        }
    }
    println(bestCost)
    return bestPath.toMutableList()
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

@Composable
fun calcDistance(nodes: List<Node>): Float {
    var sum = 0F
    for( i in 0 until nodes.size - 1) {
        var dist = (sqrt((nodes[i].x.toDouble() - nodes[i+1].x.toDouble()).pow(2.0) + (nodes[i].y.toDouble() - nodes[i+1].y.toDouble()).pow(2.0))).toFloat()
        sum += dist
    }
    return sum
}
