import java.io.File

// This is a bit of a mash-up of the following solutions
// The trickiest part was finding the right values to update after explode
// https://github.com/ArpitShukIa/AdventOfCode2021/blob/main/src/Day18.kt
// https://github.com/elizarov/AdventOfCode2021/blob/main/src/Day18.kt
// https://github.com/edgars-supe/advent-of-code/blob/master/src/main/kotlin/lv/esupe/aoc/year2021/Day18.kt

fun main() {
    println(Day18().puzzle1())
    println(Day18().puzzle2())
}

class Day18 {
    private val input = File("inputs/day18.txt").readLines()
    fun puzzle1() = input.map { parse(it) }.reduce { a, b -> a + b }.magnitude()
    fun puzzle2() = input.indices.flatMap { i -> input.indices.map { j -> i to j}}.filter {(i,j) -> i != j }.maxOf { (i,j) -> (parse(input[i]) + parse(input[j])).magnitude() }

    private fun parse(input: String): Num {
        var cur = 0

        fun _parse(): Num =
            if (input[cur] == '[') {
                cur++
                val left = _parse()
                cur++ // skip comma
                val right = _parse()
                cur++ // skip close
                Num.NumPair(left, right)
            }
            else Num.NumValue(input[cur].digitToInt()).also { cur++ }

        return _parse()
    }


    sealed class Num {
        var parent: NumPair? = null

        class NumValue(var value: Int) : Num() {
            override fun toString(): String = value.toString()

            fun canSplit(): Boolean = value >= 10
            fun split() {
                val num = NumPair(NumValue(value / 2), NumValue((value + 1) / 2))
                parent?.replaceWith(this, num)
            }
        }
        class NumPair(var left: Num, var right: Num) : Num() {
            init {
                left.parent = this
                right.parent = this
            }
            override fun toString(): String = "[$left,$right]"

            fun explode() {
                val x = if (left is NumValue) (left as NumValue).value else null
                val y = if (right is NumValue) (right as NumValue).value else null
                findValueToLeft()?.let { it.value += x!! }
                findValueToRight()?.let { it.value += y!! }
                parent?.replaceWith(this, NumValue(0))
            }

            fun replaceWith(child: Num, newValue: Num) {
                if (left == child)      { left = newValue  }
                else if (right == child){ right = newValue }

                newValue.parent = this
            }
        }


        fun magnitude(): Int = when(this) {
            is NumValue -> value
            is NumPair -> left.magnitude() * 3 + right.magnitude() * 2
        }

        operator fun plus(other: Num): Num =
            NumPair(this, other).apply {
                left.parent = this
                right.parent = this
                reduce()
            }

        fun reduce() {
            do  {
                var exploded = false
                var split = false

                findNextExplode()?.apply {
                    explode()
                    exploded = true
                }
                if (!exploded) findNextToSplit()?.apply {
                    split()
                    split = true
                }
            } while (exploded || split)
        }

        fun findValueToRight(): NumValue? {
            if (this is NumValue) return this
            if (this == parent?.left) return parent!!.right.findValueFurthestLeft()
            if (this == parent?.right) return parent!!.findValueToRight()

            return null
        }
        fun findValueToLeft(): NumValue? {
            if (this is NumValue) return this
            if (this == parent?.left) return parent!!.findValueToLeft()
            if (this == parent?.right) return parent!!.left.findValueFurthestRight()

            return null
        }
        private fun findValueFurthestLeft(): NumValue? = when(this) {
            is NumValue -> this
            is NumPair -> this.left.findValueFurthestLeft()
        }
        private fun findValueFurthestRight(): NumValue? = when(this) {
            is NumValue -> this
            is NumPair -> this.right.findValueFurthestRight()
        }

        private fun findNextToSplit(): NumValue? =
            if (this is NumValue && canSplit()) this
            else if (this is NumPair) left.findNextToSplit() ?: right.findNextToSplit()
            else null

        private fun findNextExplode(depth: Int = 0): NumPair? =
            if (this is NumPair) {
                if (depth >= 4) this
                else left.findNextExplode(depth + 1) ?: right.findNextExplode(depth + 1)
            }
            else null
    }
}