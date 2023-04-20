package uk.ac.aber.dcs.cs31620.demonstratingtsp.solvers

import uk.ac.aber.dcs.cs31620.demonstratingtsp.components.Node
import java.lang.Float.MAX_VALUE
import java.lang.Math.sqrt
import kotlin.math.pow


class HeldKarpAlgorithm(nodes: MutableList<Node> ) {

    private val n = nodes.size
    private val distance = Array(n) { FloatArray(n) }
    private val memory = Array(n) { FloatArray(n) { -1F } }

    fun main(nodes: MutableList<Node>) {
        generateMatrix(nodes)
        val mincost = findShortestPath()

    }

    private fun generateMatrix(nodes: MutableList<Node>): Array<FloatArray> {
        for (i in 0 until nodes.size) {
            for (j in 1 until nodes.size) {
                val dist = (sqrt(
                    (nodes[i].x.toDouble() - nodes[j].x.toDouble()).pow(2.0) + (nodes[i].y.toDouble() - nodes[j].y.toDouble()).pow(
                        2.0
                    )
                )).toFloat()
                distance[i][j] = dist
            }
        }
        return distance       //MATRIX OF COSTS
    }

    private fun findShortestPath() {
        for(i in distance.indices) {
            memory[0][i] = distance[0][i]   //STORES THE DISTANCES FROM 1 TO THE REST OF NODES
        }

        val subset = mutableListOf<Int>()
        var minVal = MAX_VALUE
        for(j in 1 until distance.size) {   //ADDS NODES TO SUBSET
            subset.add(j)
        }

        for(item in subset - 1) {
            val copySubset = subset.toMutableList()
            copySubset.remove(item)
            for(k in copySubset) {
                val tempMin = distance[item][k] + getCost(item, k, copySubset)
                if(minVal < tempMin) {
                    minVal = tempMin
                }
            }
        }
        println(minVal)
    }

    private fun getCost(prevNode:Int, k: Int, subset: MutableList<Int>): Float {
        var cost = MAX_VALUE
        if (memory[prevNode][k] == -1F) {
            val copySubset = subset.toMutableList()
            if (copySubset.isNotEmpty()) {
                for (j in 0 until copySubset.size - 1) {
                    println("BEFORE REMOVE")
                    println(k)
                    println(j)
                    println(copySubset)
                    copySubset.removeAt(j)
                    println("AFTER REMOVE")
                    println(k)
                    println(j)
                    println(copySubset)
                    getCost(k, j, copySubset)
                }
            } else {
                println("Error")
            }
        }
        if(subset.isEmpty()) {
            cost = distance[prevNode][k]
        }
        return cost
    }

    fun backtrack() {

    }


}
