import java.io.File

fun main() {
    Day1().puzzle1()
    Day1().puzzle2()
}

class Day1 {
    fun puzzle1() {
        val file = File("day1.txt")
        val lines = file.readLines().map { it.toInt() }

        val increases = lines.windowed(2, 1) {
            if (it[1] > it[0] ) 1
            else 0
        }.sum()

        println(increases)
    }

    fun puzzle2() {
        val file = File("day1.txt")
        val lines = file.readLines().map { it.toInt() }

        val increases = lines.windowed(4, 1) {
            val w1 = it[0]+it[1]+it[2]
            val w2 = it[1]+it[2]+it[3]

            if (w2 > w1 ) 1
            else 0
        }.sum()

        println(increases)
    }
}