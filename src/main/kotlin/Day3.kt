import java.io.File

fun main() {
    Day3().puzzle1()
    Day3().puzzle2()
}

class Day3 {

    val lines = File("inputs/day3.txt").readLines()

    fun puzzle1() {
        val matrix = lines.map { s -> s.toCharArray().map { c -> c.digitToInt() } }
        var gamma = ""
        var epsilon = ""

        for(idx in matrix[0].indices) {
            val res = matrix.partition { it[idx] == 1 }
            gamma += if (res.first.count() > res.second.count())  "1" else "0"
            epsilon +=  if (res.first.count() > res.second.count())  "0" else "1"
        }

        val answer = gamma.toInt(2) * epsilon.toInt(2)
        println(answer)
    }

    fun puzzle2() {
        fun splitAndRecurse(list: List<List<Int>>, index: Int, max: Boolean) : List<Int> {
            if (list.size == 1) return list[0]

            val res = list.partition { it[index] == 1 }
            val keep =
                if (res.first.count() >= res.second.count())
                    if (max) res.first else res.second
                else
                    if (max) res.second else res.first
            return splitAndRecurse(keep, index+1,  max)
        }

        val matrix = lines.map { s -> s.toCharArray().map { c -> c.digitToInt() } }
        val oxygen = splitAndRecurse(matrix, 0, true)
        val co2 = splitAndRecurse(matrix, 0, false)

        val oxygenDecimal = oxygen.joinToString("").toInt(2)
        val co2Decimal = co2.joinToString("").toInt(2)

        println(oxygenDecimal * co2Decimal)
    }
}