import java.io.File
import java.util.concurrent.atomic.AtomicInteger


fun main() {
    part1()
    part2()
}

private fun part1() {
    val startPos = File("inputs/day21.txt").readLines().map { it.last().digitToInt() }
    val scores = intArrayOf(0, 0)
    val space = startPos.toIntArray()
    val dice = AtomicInteger()
    var diceRolls = 0

    fun nextRoll(): Int {
        if (dice.get() >= 100) dice.set(0)
        diceRolls++
        return dice.incrementAndGet()
    }

    while (true) {
        repeat(2) {
            val roll = nextRoll() + nextRoll() + nextRoll()
            space[it] = (space[it] + roll) % 10
            scores[it] += if (space[it] != 0) space[it] else 10

            if (scores[it] >= 1000) {
                println(scores[(it + 1) % 2] * diceRolls) // other player score * diceRolls
                return
            }
        }
    }
}

// After a few failed attempts at Part2 - found a lots of similar solutions for part2 on Github
// most understandable was here --> https://github.com/ArpitShukIa/AdventOfCode2021/blob/main/src/Day21.kt
// does a 3 dimensional loop and solves for each. If score does not reach 21 for either player, the calculation
// is repeated and the 3 dimensional loop is processed again (and again and again if needed)
// player turns are dealt with by always processing player 1, but flipping who is player one
fun part2() {
    data class Universe(val p1: Int, val p2: Int, val s1: Int, val s2: Int)

    val startPos = File("inputs/day21.txt").readLines().map { it.last().digitToInt() }

    val dp = mutableMapOf<Universe, Pair<Long, Long>>()
    fun solve(u: Universe): Pair<Long, Long> {
        dp[u]?.let { return it }
        if (u.s1 >= 21) return 1L to 0L
        if (u.s2 >= 21) return 0L to 1L
        var ans = 0L to 0L
        for (d1 in 1..3) for (d2 in 1..3) for (d3 in 1..3) {
            val newP1 = (u.p1 + d1 + d2 + d3 - 1) % 10 + 1
            val newS1 = u.s1 + newP1
            val (x, y) = solve(Universe(u.p2, newP1, u.s2, newS1))
            ans = ans.first + y to ans.second + x
        }
        return ans.also { dp[u] = it }
    }
    println( solve(Universe(startPos[0], startPos[1], 0, 0)).let { maxOf(it.first, it.second) } )
}