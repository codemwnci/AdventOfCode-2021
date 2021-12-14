import java.io.File

fun main() {
    Day14().puzzle1()
    Day14().puzzle2()
}

class Day14 {
    val lines = File("inputs/day14.txt").readLines()
    val polyTemplate = lines.first()

    fun puzzle1() {
        val pairInsertions = lines.drop(2).map {
            val (pair, insertion) = it.split(" -> ")
            pair to insertion
        }

        var polyIteration = polyTemplate
        for (step in 1..10) {
            polyIteration = polyIteration.fold("") { str, curr ->
                val pairCheck = "" + (str.lastOrNull() ?: "") + curr
                val insert = pairInsertions.find { it.first == pairCheck } ?.second ?: ""

                str + insert + curr
            }
            //println("Step $step = $polyIteration")
        }

        val elementCounts = polyIteration.groupBy { it }.map { it.key to it.value.size }.sortedBy { it.second }
        val ans = elementCounts.last().second - elementCounts.first().second
        //println(elementCounts)
        println("Puzzle 1 Answer: $ans")

    }

    // Pairing approach inspired by https://github.com/arpitShukIa/AdventOfCode2021/blob/main/src/Day14.kt
    fun puzzle2() {
        val insertPattern = lines.drop(2).map { it.split(" -> ") }.associate { it[0] to it[1] }
        var pairFrequency = mutableMapOf<String, Long>()

        // instead of going through each pairing, with all the duplicates
        // just create a frequency map and then create new pairings based on the frequencies
        // e.g if you have 50 of AB, and AB requires an X in the middle
        // then we now have 50 of AX and 50 of XB.
        // This will be far more efficient than puzzle1 answer, which takes 4x time each step
        polyTemplate.windowed(2).forEach{ pairFrequency.put(it, pairFrequency.getOrDefault(it, 0) + 1) }
        for (step in 1..40) {
            val updatedFrequencies = mutableMapOf<String, Long>()
            pairFrequency.forEach {
                // we don't just set value to it.value, because there may be other pairs the same
                // so we increment
                val key1 = it.key[0] + insertPattern[it.key]!!
                val key2 = insertPattern[it.key]!! + it.key[1]
                updatedFrequencies.put(key1, updatedFrequencies.getOrDefault(key1, 0) + it.value)
                updatedFrequencies.put(key2, updatedFrequencies.getOrDefault(key2, 0) + it.value)
            }
            pairFrequency = updatedFrequencies
        }

        // we are now left with all the pairs and their frequencies, but we need to know the individual
        // characters and their frequencies, so we now need to iterate through the pair list, and add up
        // all the frequencies
        // NOTE: when counting single chars 50 Xs in the middle of AB (as per example above) (50 AX and 50 XB should not result in 100 Xs, but 50 Xs)
        val charFrequency = pairFrequency.toList().fold(mutableMapOf<Char, Long>()) { acc, pair ->
            acc.put(pair.first[0], acc.getOrDefault(pair.first[0], 0) + pair.second)
            acc
        }

        // because we have been working in pairs, don't forget to increment the final character into the frequency map
        charFrequency.put(polyTemplate.last(), charFrequency.getOrDefault(polyTemplate.last(), 0) + 1)

        val sorted = charFrequency.values.sorted()
        val ans = sorted.last() - sorted.first()

        println("Puzzle 2 Answer: $ans")
    }
}