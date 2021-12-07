import java.io.File
import kotlin.math.abs

fun main() {
    Day7().puzzle1()
    Day7().puzzle2()
}

class Day7 {
    val crabs = File("inputs/day7.txt").readLines().first().split(",").map{it.toInt()}

    fun puzzle1() {
        val min = crabs.minOrNull()!!
        val max = crabs.maxOrNull()!!

        var lowest = Int.MAX_VALUE
        var lowestIndex = -1

        for(i in min .. max) {
            var fuelUsed = 0
            // calc fuel use for each position between min and max
            for(crab in crabs) { fuelUsed += abs(crab - i) }

            if (fuelUsed < lowest) {
                lowest = fuelUsed
                lowestIndex = i
            }
        }

        println("Lowest Fuel: $lowest, at position $lowestIndex")

    }

    fun puzzle2() {

        val min = crabs.minOrNull()!!
        val max = crabs.maxOrNull()!!

        var lowest = Int.MAX_VALUE
        var lowestIndex = -1

        for(i in min .. max) {
            var fuelUsed = 0
            // calc fuel use for each position between min and max
            for(crab in crabs) { fuelUsed += calcFibIncrement(abs(crab - i)) }

            if (fuelUsed < lowest) {
                lowest = fuelUsed
                lowestIndex = i
            }
        }

        println("Lowest Fuel: $lowest, at position $lowestIndex")
    }

    fun calcFibIncrement(increment: Int, incrementBy: Int = 0): Int {
        return if (increment == 0)
            incrementBy
        else
            calcFibIncrement(increment - 1, incrementBy + 1) + incrementBy
    }

}