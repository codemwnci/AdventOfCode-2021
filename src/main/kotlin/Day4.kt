import java.io.File


fun main() {
    Day4().puzzle1()
    Day4().puzzle2()
}

class Day4 {

    private val lines = File("day4.txt").readLines()

    private val drawOrder = lines[0].split(",").map { it.toInt() }
    private val boards = lines.drop(1).windowed(6, 6) {
        // drop first line is a separator
        it.drop(1).map {
            line -> line.trim().split("  ", " ").map {
                numStr -> numStr.toInt()
            }
        }
    }

    fun checkLine(line: List<Int>, drawn: List<Int>) = drawn.containsAll(line)
    fun checkBoard(board: List<List<Int>>, drawn: List<Int>): Boolean {

        // check horizontals first
        val hasHorizontalLine = board.any { checkLine(it, drawn) }
        // check vertical next by creating a new 2d array with the indexes rotated
        val flippedBoard = ( board[0].indices ).map { outer ->
            ( board.indices ).map { inner ->
                board[inner][outer]
            }
        }
        val hasVerticalLine = flippedBoard.any { checkLine(it, drawn) }

        return hasHorizontalLine || hasVerticalLine
    }

    fun puzzle1() {

        for (i in 5..drawOrder.size) {
            val currentDraw = drawOrder.take(i)
            boards.forEach() {
                if (checkBoard(it, currentDraw)) {
                    return calculateWinner(it, currentDraw)
                }
            }
        }
    }

    private fun calculateWinner(board: List<List<Int>>, currentDraw: List<Int>) {
        println(board.flatten().filter { !currentDraw.contains(it) }.sum() * currentDraw.last())
    }

    fun puzzle2() {
        val winners = mutableListOf<Int>()

        for (i in 5..drawOrder.size) {
            val currentDraw = drawOrder.take(i)
            boards.forEachIndexed() { index, board ->
                // don't recheck previous winners
                if (!winners.contains(index) && checkBoard(board, currentDraw)) {
                    // first, add to the list of winners
                    winners.add(index)
                    // if all have now won, then do end game calcs
                    if (winners.size == boards.size) {
                        return calculateWinner(board, currentDraw)
                    }
                }
            }
        }
    }
}