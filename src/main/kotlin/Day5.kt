import java.io.File
import kotlin.math.abs


fun main() {
    //Day5().puzzle1()
    Day5().puzzle2()
}

class Day5 {
    data class Point(val x:Int, val y: Int)

    private val lines = File("inputs/day5.txt").readLines()
    private val points = lines.map { line ->
        line.split(" -> ").map { point ->
            val pairs = point.split(",")
            Point(pairs[0].toInt(), pairs[1].toInt())
        }
    }

    fun puzzle1() {

        val map2d = Array(1000) { Array(1000) { 0 } }

        points.forEach() {
            // only consider horizontal and vertical
            if (it[0].x == it[1].x) {
                val min = Math.min(it[0].y, it[1].y)
                val max = Math.max(it[0].y, it[1].y)

                for(i in min..max) {
                    // increment all along the Y axis
                    map2d[i][it[0].x]++
                }
            }
            else if (it[0].y == it[1].y) {
                val min = Math.min(it[0].x, it[1].x)
                val max = Math.max(it[0].x, it[1].x)

                for(i in min..max) {
                    // increment all along the Y axis
                    map2d[it[0].y][i]++
                }
            }
        }

//        map2d.forEach {
//            println("")
//            it.forEach { print(it) }
//        }

        val overlaps = map2d.sumOf { it.count { point -> point >= 2 } }

        println(overlaps)

    }

    fun puzzle2() {

        val map2d = Array(1000) { Array(1000) { 0 } }

        points.forEach() {
            // horizontal
            if (it[0].x == it[1].x) {
                val min = Math.min(it[0].y, it[1].y)
                val max = Math.max(it[0].y, it[1].y)

                for(i in min..max) {
                    // increment all along the Y axis
                    map2d[i][it[0].x]++
                }
            }
            // vertical
            else if (it[0].y == it[1].y) {
                val min = Math.min(it[0].x, it[1].x)
                val max = Math.max(it[0].x, it[1].x)

                for(i in min..max) {
                    // increment all along the Y axis
                    map2d[it[0].y][i]++
                }
            }
            // diagonal
            else if ( isDiagonal(it[0], it[1]) ) {
                val len = abs(it[0].x - it[1].x)
                val xStep = if (it[1].x - it[0].x > 0) 1 else -1
                val yStep = if (it[1].y - it[0].y > 0) 1 else -1

                for(i in 0..len) {
                    val y = it[0].y + (i*yStep)
                    val x = it[0].x + (i*xStep)
                    map2d[y][x]++
                }
            }
        }

//        map2d.forEach {
//            println("")
//            it.forEach { print(it) }
//        }

        val overlaps = map2d.sumOf { it.count { point -> point >= 2 } }

        println(overlaps)
    }

    private fun isDiagonal(p1: Point, p2: Point): Boolean = abs(p1.x - p2.x) == abs(p1.y - p2.y)
}