import java.io.File

fun main() {
    Day2().puzzle1()
    Day2().puzzle2()
}

class Day2 {
    val lines = File("day2.txt").readLines()

    fun puzzle1() {
        var horizontal = 0
        var depth = 0

        lines.forEach() {
            val instructions = it.split(" ")
            val direction = instructions[0]
            val amount = instructions[1].toInt()

            when (direction) {
                "forward" -> horizontal += amount
                "down" -> depth += amount
                "up" -> depth -= amount
            }
        }
        println("Horizontal $horizontal : Depth $depth")
        println(horizontal * depth)
    }

    fun puzzle2() {
        var horizontal = 0
        var depth = 0
        var aim = 0

        lines.forEach() {
            val instructions = it.split(" ")
            val direction = instructions[0]
            val amount = instructions[1].toInt()

            when (direction) {
                "forward" -> {
                    horizontal += amount
                    depth += aim * amount
                }
                "down" -> aim += amount
                "up" -> aim -= amount
            }
        }
        println("Horizontal $horizontal : Depth $depth")
        println(horizontal * depth)
    }
}