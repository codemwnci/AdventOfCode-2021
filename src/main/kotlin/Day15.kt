import java.io.File
import java.util.*

fun main() {
    Day15().puzzle1()
    Day15().puzzle2()
}

class Day15 {
    data class Node(val x:Int = 0, val y:Int = 0, val dist: Int = 0)

    fun puzzle1() {

        val grid = File("inputs/day15.txt").readLines().map { it.toCharArray().map{ it.digitToInt() } }
        findShortestPath(grid)
    }

    private fun findShortestPath(grid: List<List<Int>>) {
        // initially, build a node graph with cost between each node
        // keep a record of shortest path to a particular node, and if
        // we exceed it in our search, then abandon the search
        // This is known as Dijkstra's - more info here -> https://medium.com/@urna.hybesis/pathfinding-algorithms-the-four-pillars-1ebad85d4c6b
        val pathways =  Array(grid.size) { Array(grid[0].size) { Int.MAX_VALUE } }
        val queue = PriorityQueue<Node> { nodeA, nodeB -> nodeA.dist - nodeB.dist } // the function is how to compared priorities between nodes in a queue

        pathways[0][0] = 0 // start needs to be set to zero
        queue.add(Node(0,0, 0))

        while(queue.isNotEmpty()) {
            val (x, y, dist) = queue.poll()
            // search neighbours
            listOf(x to y + 1, x to y - 1, x + 1 to y, x - 1 to y).forEach { (X, Y) ->
                // if any of the (valid) neighbours have a shorter path to this node
                // then reduce the distance for this node to the new value
                // and add the winning node to the queue for further searching
                // any losing paths will be ignored / discarded
                if (X in grid.indices && Y in grid[0].indices && pathways[X][Y] > dist + grid[X][Y]) {
                    pathways[X][Y] = dist + grid[X][Y]
                    queue.add(Node(X, Y, pathways[X][Y]))
                }
            }
        }

        println(pathways.last().last())
    }


    fun puzzle2() {
        val input = File("inputs/day15.txt").readLines()
        val totalY = input.size
        val totalX = input.first().length

        val grid = (0 until totalY * 5).map { y ->
            (0 until totalX * 5).map { x ->
                // we are now filling up a grid that is 25X the size (5x5)
                // the value increases by the number of X spaces and number of Y spaces away
                // so if the value is 8, and it is 1 X tile and 1 Y tile space away, the value would be 10
                // except any value above 9 rolls over back to 1
                val baseNum = input[y % totalY][x % totalX].digitToInt()
                val tileDistance = (x/totalX) + (y/totalY)

                if (baseNum + tileDistance < 10) baseNum + tileDistance else (baseNum + tileDistance) - 9
            }
        }

        findShortestPath(grid)
    }
}