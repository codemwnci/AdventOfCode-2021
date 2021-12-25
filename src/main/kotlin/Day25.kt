import java.io.File

fun main() {

    var world = File("inputs/day25.txt").readLines().map { it.toCharArray() }.toTypedArray()

    fun step(): Boolean {
        val xSize = world[0].size
        val ySize = world.size

        fun moveHerd(herd: Char): Boolean {
            val xMove = if (herd == '>') 1 else 0
            val yMove = if (herd == 'v') 1 else 0

            var hasMoved = false
            var newWorld = Array(ySize) { CharArray(xSize) {'.'} }
            world.forEachIndexed { y, chars -> chars.forEachIndexed { x, c ->
                if (c == herd) {
                    val newX = (x+xMove) % xSize
                    val newY = (y+yMove) % ySize
                    if (world[newY][newX] == '.') {
                        newWorld[newY][newX] = c
                        hasMoved = true
                    }
                    else {
                        newWorld[y][x] = c
                    }
                }
                else if (c != '.') newWorld[y][x] = c
            } }
            world = newWorld
            return hasMoved
        }

        // cannot use short-circuit OR here || because we need both movements to evaluate
        return (moveHerd('>') or moveHerd('v'))
    }

    // process all steps until they stop moving
    var steps = 0
    do {
        steps++
        //world.print()
    } while (step())

    println("\n\nAnswer: $steps steps")

    // output the full map of the seacucumbers
    fun Array<CharArray>.print() = println(this.map { it.joinToString("")+"\n" }.joinToString(""))
}