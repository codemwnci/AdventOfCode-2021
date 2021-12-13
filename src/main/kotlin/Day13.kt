import java.io.File

fun main() {
    Day13().puzzle1()
    Day13().puzzle2()
}

class Day13 {
    private val lines = File("inputs/day13.txt").readLines()
    private val coords = lines.takeWhile { it.trim() != "" }.map {
        val (x, y) = it.split(",")
        x.toInt() to y.toInt()
    }
    private val folds = lines.drop(coords.size + 1)

    fun puzzle1() {

        val maxX = coords.maxOf { it.first }
        val maxY = coords.maxOf { it.second }

        val yFold = folds.first().startsWith("fold along y")
        val foldAmount = folds.first().split("=")[1].toInt()

        val afterFold = if (yFold) {
                // take all above the fold
                val newCoords = coords.filter { it.second < foldAmount }.toMutableList()
                // loop through those below the fold (those ON the fold are ignored
                for (pair in coords.filter {it.second > foldAmount} ) {
                    newCoords.add(pair.first to maxY - pair.second)
                }
                newCoords
            }
            else  {
                val newCoords = coords.filter { it.first < foldAmount }.toMutableList()
                // loop through those right of the fold (those ON the fold are ignored
                for (pair in coords.filter {it.first > foldAmount} ) {
                    newCoords.add(maxX - pair.first to pair.second)
                }
                newCoords
            }

        println(afterFold.distinct().size)
    }

    fun puzzle2() {

        var foldedCoords = coords
        folds.forEach { fold ->
            val maxX = foldedCoords.maxOf { it.first }
            val maxY = foldedCoords.maxOf { it.second }

            val yFold = fold.startsWith("fold along y")
            val foldAmount = fold.split("=")[1].toInt()

            val afterFold = if (yFold) {
                // take all above the fold
                val newCoords = foldedCoords.filter { it.second < foldAmount }.toMutableList()
                // loop through those below the fold (those ON the fold are ignored
                for (pair in foldedCoords.filter {it.second > foldAmount} ) {
                    newCoords.add(pair.first to maxY - pair.second)
                }
                newCoords
            }
            else  {
                val newCoords = foldedCoords.filter { it.first < foldAmount }.toMutableList()
                // loop through those right of the fold (those ON the fold are ignored
                for (pair in foldedCoords.filter {it.first > foldAmount} ) {
                    newCoords.add(maxX - pair.first to pair.second)
                }
                newCoords
            }

            foldedCoords = afterFold
        }

        // print coords
        val maxX = foldedCoords.maxOf { it.first }
        val maxY = foldedCoords.maxOf { it.second }

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                print( if ((x to y) in foldedCoords) "#" else ".")
            }
            println()
        }
    }
}