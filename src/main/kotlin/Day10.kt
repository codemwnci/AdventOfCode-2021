import java.io.File
import java.util.*

fun main() {
    Day10().puzzle1()
    Day10().puzzle2()
}

class Day10 {
    private val lines = File("inputs/day10.txt").readLines()
    private val opening = arrayOf('{', '[', '(', '<')
    private val bracketPairs = mapOf('{' to '}', '[' to ']', '(' to ')', '<' to '>')

    private fun checkMatchingBrackets(stack: Stack<Char>, remainingChars: String): Int {
        if (remainingChars.isEmpty()) return 0

        val c = remainingChars.first()
        if (c in opening) {
            stack.push(c)
        }
        else {
            val matchTo = stack.pop()
            if (bracketPairs[matchTo] != c) {
                // if the bracket does not match it's pair, then we have found an error
                return when(c) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> 0
                }
            }
        }
        // keep recursing through the string if we've not found an error
        return checkMatchingBrackets(stack, remainingChars.substring(1))
    }

    fun puzzle1() {
        val total = lines.sumOf {
            checkMatchingBrackets(Stack<Char>(), it)
        }
        println(total)
    }

    fun puzzle2() {
        val total = lines.map {
            val stack = Stack<Char>()
            if (checkMatchingBrackets(stack, it) == 0) {
                // stack should contain the unclosed brackets
                stack.foldRight(0L) { c, acc ->
                    acc * 5 + when(bracketPairs[c]) {
                        ')' -> 1
                        ']' -> 2
                        '}' -> 3
                        '>' -> 4
                        else -> 0
                    }
                }
            }
            else 0
        }
        .filterNot { it == 0L }
        .sorted()

        // print the middle index
        println(total[(total.size / 2)])
    }
}