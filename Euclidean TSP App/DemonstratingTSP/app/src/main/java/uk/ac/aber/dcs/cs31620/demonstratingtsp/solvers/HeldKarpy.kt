package uk.ac.aber.dcs.cs31620.demonstratingtsp.solvers

import androidx.compose.runtime.Composable
import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.Node
import java.lang.Float.min
import kotlin.math.pow
import kotlin.math.sqrt

class HeldKarpy(circles: MutableList<Node>) {

    private val n = circles.size
    private val distance = Array(n) { FloatArray(n) }
    private val best = Array(1 shl(n - 1)) { FloatArray(n) { Float.MAX_VALUE } }
    private val path = Array(1 shl(n - 1)) { IntArray(n) { -1 } }

    init {
        generateMatrix(circles)
    }


    fun shortPath(): Pair<Float, List<Int>> {
        for(visited in 1 until (1 shl (n-1))) {
            for(last in 0 until (n-1)) {
                if((visited and (1 shl last)) == 0) { continue }
                if(visited == 1 shl last) {
                    best[visited][last] = distance[n-1][last]
                } else {
                    var prevVisited = visited xor (1 shl last)
                    for(prev in 0 until n-1) {
                        if ((prevVisited and (1 shl prev)) == 0) {continue}
                        val newDist = distance[last][prev] + best[prevVisited][prev]
                        if (newDist < best[visited][last]) {
                            best[visited][last] = newDist
                            path[visited][last] = prev
                        }
                    }
                }
            }
        }

        var lastNode = -1
        var answer = Float.MAX_VALUE
        for(last in 0 until n - 1 ) {
            val newDist = distance[last][n - 1] + best[(1 shl (n - 1)) - 1][last]
            if (newDist < answer) {
                answer = newDist
                lastNode = last
            }
        }

        var currentNode = lastNode
        var ifVisited = (1 shl (n-1)) - 1
        val pathList = mutableListOf(currentNode + 1) // add 1 to node indices to match circle indices

        while (ifVisited != 0) {
            val prevNode = path[ifVisited][currentNode]
            pathList.add(0, prevNode + 1) // add 1 to node indices to match circle indices
            ifVisited = ifVisited xor (1 shl currentNode)
            currentNode = prevNode
        }

        pathList.add(pathList[0])
        answer + distance[pathList[0]][0]

        println("ANSWER HERE")
        println(answer)
        println("pathList")
        println(pathList)

        return Pair(answer, pathList)
    }

    private fun generateMatrix(nodes: MutableList<Node>): Array<FloatArray> {
        for (i in 0 until nodes.size) {
            for (j in 1 until nodes.size) {
                val dist = (Math.sqrt(
                    (nodes[i].x.toDouble() - nodes[j].x.toDouble()).pow(2.0) + (nodes[i].y.toDouble() - nodes[j].y.toDouble()).pow(
                        2.0
                    )
                )).toFloat()
                distance[i][j] = dist       //POTENTIALLY ASSIGN distance[i][j] to distance[j][i] WILL THIS DECREASE STORAGE? MAYBE?
            }
        }
        return distance       //MATRIX OF COSTS
    }
}