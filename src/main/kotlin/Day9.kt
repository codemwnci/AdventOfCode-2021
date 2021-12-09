import java.io.File

fun main() {
    Day9().puzzle1()
    Day9().puzzle2()
}

class Day9 {

    data class Point(val x:Int, val y:Int) {
        fun neighbours(xBoundary: Int, yBoundary: Int): List<Point> =
            listOf(Point(x-1, y), Point(x+1, y), Point(x, y-1), Point(x, y+1) ).filter {
                it.x in 0 until xBoundary && it.y  in 0 until yBoundary
            }
    }

    val heightmap = File("inputs/day9.txt").readLines().map { it.toCharArray().map { c -> c.digitToInt() } }

    fun puzzle1() {
        val total = heightmap.foldIndexed(0) { yIndex, acc, list ->
            list.foldIndexed(acc) { xIndex, sum, height ->
                // if the surrounding tiles are not smaller, then add the tile to the running tally
                // otherwise leave the tally alone
                if (Point(xIndex, yIndex).neighbours(heightmap[0].size, heightmap.size).count { heightmap[it.y][it.x] <= height } == 0)
                    sum + heightmap[yIndex][xIndex] + 1
                else sum

// replaced this code with the neighbours function of the Point class, making the code more functional in nature
// leaving this here for comparison purposes
//                if (height == 0 ||
//                    (xIndex == 0 || heightmap[yIndex][xIndex-1] > height) &&
//                    (yIndex == 0 || heightmap[yIndex-1][xIndex] > height) &&
//                    (xIndex == heightmap[yIndex].lastIndex || heightmap[yIndex][xIndex+1] > height) &&
//                    (yIndex == heightmap.lastIndex || heightmap[yIndex+1][xIndex] > height)
//                ) sum + heightmap[yIndex][xIndex] + 1 else sum
            }
        }

        println("Total: $total")
    }

    fun puzzle2() {

        fun getLowPoints() = heightmap.flatMapIndexed { yIndex, list ->
            list.mapIndexed { xIndex, height ->
                val smallerNeighbours = Point(xIndex, yIndex).neighbours(heightmap[0].size, heightmap.size).count { heightmap[it.y][it.x] <= height }
                if (smallerNeighbours == 0) Point(xIndex, yIndex) else null
            }.filterNotNull()
        }

        // some of this code inspired by https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day09.kt
        fun getBasinSize(p: Point): Int {
            val visited = mutableSetOf(p)
            val queue = mutableListOf(p)
            while (queue.isNotEmpty()) {
                val newNeighbors = queue.removeFirst()
                    .neighbours(heightmap[0].size, heightmap.size)
                    .filter { it !in visited }
                    .filter { heightmap[it.y][it.x] != 9 }
                visited.addAll(newNeighbors)
                queue.addAll(newNeighbors)
            }
            return visited.size
        }

        // remember that reduce for the 3 items will multiply item 1 by item2, and then the total of that will pass
        // to the next reduce, and be total * item3
        val answer = getLowPoints().map { getBasinSize(it) }
            .sortedDescending()
            .take(3)
            .reduce { total, next -> total * next }

        println(answer)

    }
}