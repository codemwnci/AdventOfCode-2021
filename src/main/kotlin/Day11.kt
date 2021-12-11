import java.io.File

fun main() {
    Day11().puzzle1()
    Day11().puzzle2()
}

class Day11 {

    private val octogrid = File("inputs/day11.txt").readLines().map { it.toCharArray().map { c -> c.digitToInt() }.toMutableList() }.toMutableList()
    data class GridPoint(val x:Int, val y:Int) {
        fun neighbours(xBoundary: Int, yBoundary: Int): List<GridPoint> =
            (-1 .. 1).map { yOffset ->  (-1 .. 1).map { xOffset -> GridPoint(x+xOffset, y+yOffset)  } }.flatten()
                .filter {
                    // exclude points that go outside the boundary, and exclude self
                    it.x in 0 until xBoundary && it.y  in 0 until yBoundary && !(it.x == x && it.y == y)
                }
    }

    fun puzzle1() {
        var totalFlashes = 0
        for(step in 1..100) {

            val flashed = mutableListOf<GridPoint>()
            // first, increase energy level of all by 1
            for (y in octogrid.indices) {
                for (x in octogrid[y].indices) {
                    octogrid[y][x]++
                }
            }

            // keep looping through until all that can flash have flashed
            do {
                val newFlashes = mutableListOf<GridPoint>()
                for (y in octogrid.indices) {
                    for (x in octogrid[y].indices) {
                        val gp = GridPoint(x, y)
                        if (octogrid[y][x] > 9 && gp !in flashed) {
                            newFlashes.add(gp)
                            gp.neighbours(octogrid[y].size, octogrid.size).forEach {
                                // don't worry if we increase the value of already flashed octos, we won't "reflash" them
                                // and they will get reset at the end of the step anyway
                                octogrid[it.y][it.x]++
                            }
                        }
                    }
                }
                flashed.addAll(newFlashes)
            } while(newFlashes.isNotEmpty())

            totalFlashes += flashed.size

            // now reset flashed
            flashed.forEach { octogrid[it.y][it.x] = 0 }
        }

        println(totalFlashes)
    }

    fun puzzle2() {

        val totalOctos = octogrid.flatten().size

        var step = 0

        do {
            step++

            val flashed = mutableListOf<GridPoint>()
            // first, increase energy level of all by 1
            for (y in octogrid.indices) {
                for (x in octogrid[y].indices) {
                    octogrid[y][x]++
                }
            }

            // keep looping through until all that can flash have flashed
            do {
                val newFlashes = mutableListOf<GridPoint>()
                for (y in octogrid.indices) {
                    for (x in octogrid[y].indices) {
                        val gp = GridPoint(x, y)
                        if (octogrid[y][x] > 9 && gp !in flashed) {
                            newFlashes.add(gp)
                            gp.neighbours(octogrid[y].size, octogrid.size).forEach {
                                // don't worry if we increase the value of already flashed octos, we won't "reflash" them
                                // and they will get reset at the end of the step anyway
                                octogrid[it.y][it.x]++
                            }
                        }
                    }
                }
                flashed.addAll(newFlashes)
            } while(newFlashes.isNotEmpty())

            // now reset flashed
            flashed.forEach { octogrid[it.y][it.x] = 0 }

        } while(flashed.size != totalOctos)

        println("All flashed at step $step")
    }
}