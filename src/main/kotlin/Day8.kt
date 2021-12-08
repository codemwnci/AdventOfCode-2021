import java.io.File

fun main() {
    Day8().puzzle1()
    Day8().puzzle2()
}

class Day8 {
    private val lines = File("inputs/day8.txt").readLines().map { it.split("|")[0].trim() to it.split("|")[1].trim() }

    fun puzzle1() {
        val uniqueLengths = listOf(2, 3, 4, 7)

        var totalUnique = 0
        for((_, output) in lines) {
            output.split(" ").forEach {
                if(it.length in uniqueLengths) totalUnique++
            }
        }
        println(totalUnique)
    }

    fun puzzle2() {
        // Display values can be deduced based on the following logic
        // 1 is of length 2
        // 4 is of length 4
        // 7 is of length 3
        // 8 is of length 8
        // 9 is 6 chars contains 4
        // 0 is 6 chars contains 7, but not 4
        // 6 is 6 chars NOT contains 1
        // 3 is 5 chars and contains 1
        // 5 is 5 chars and contained within 9,
        // 2 is 5 chars and not contained within 9

        var total = 0
        for((input, output) in lines) {
            val displays = mutableMapOf<String, Int>()
            val inputs = input.split(" ").map{ it.sortChars() }

            val one = inputs.first { it.length == 2 }
            val four = inputs.first { it.length == 4 }
            val seven = inputs.first { it.length == 3 }
            val eight = inputs.first { it.length == 7 }
            val nine = inputs.first { it.length == 6 && it.containsAll(four) }

            displays[one] = 1
            displays[four] = 4
            displays[seven] = 7
            displays[eight] = 8
            displays[nine] = 9
            displays[inputs.first { it.length == 6 && it.containsAll(seven) && !it.containsAll(four) }] = 0
            displays[inputs.first { it.length == 6 && !it.containsAll(one) }] = 6
            displays[inputs.first { it.length == 5 && it.containsAll(one) }] = 3
            displays[inputs.first { it.length == 5 && !it.containsAll(one) && nine.containsAll(it) }] = 5
            displays[inputs.first { it.length == 5 && !it.containsAll(one) && !nine.containsAll(it) }] = 2

            total += output.split(" ").fold("") { number, outputVal ->
                number + displays[outputVal.sortChars()].toString()
            }.toInt()
        }

        println(total)
    }

    private fun String.sortChars() = this.toCharArray().sorted().joinToString("")
    private fun String.containsAll(chars: String) = this.toList().containsAll(chars.toList())
}