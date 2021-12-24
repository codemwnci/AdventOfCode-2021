import java.io.File

// Technically this works - but it is incredibly slow (even with the chunk-based optimisations)
// Companion object would be better extracting to a separate object (ALU maybe)
// May attempt to refactor and optimise
fun main() {
    Day24().part1()
    Day24().part2()
}

class Day24 {
    sealed class Instruction {
        fun process() {
            when (this) {
                is Input -> {
                    variables[this.variable] = readNextInput()
                }
                is Operation -> {
                    val aVal = variables[a]!!                              // a is always a variable name
                    val bVal = b.toIntOrNull() ?: variables[b.first()]!!   // b is a number of variable name

                    variables[a] = when (this) {
                        is Add -> aVal + bVal
                        is Mulitply -> aVal * bVal
                        is Divide -> if (bVal != 0) aVal / bVal else aVal          // don't allow divide by zero - skip otherwise
                        is Mod -> if (aVal>=0 && bVal>0) aVal % bVal else aVal      // don't allow a<0 or b<=0 in Mod
                        is Equals -> if (aVal == bVal) 1 else 0
                        else -> error("Unknown Instruction Found")
                    }
                }
            }
        }

        companion object {
            fun isValidSerial() = variables['z'] == 0

            var variables = mutableMapOf<Char, Int>('w' to 0, 'x' to 0, 'y' to 0, 'z' to 0)
            var input = ""
            var inputIndex = 0
            private fun readNextInput(): Int {
                return input[inputIndex++].digitToInt()
            }
            fun setNewSerial(serial: String) {
                input = serial
                inputIndex = 0
                variables = mutableMapOf<Char, Int>('w' to 0, 'x' to 0, 'y' to 0, 'z' to 0)
                skiptoNextInput = false
            }

            // consider saving the variable states (the set),before the next input is read, so
            // we don't have to reprocess the same calculations when the output will be the same
            // e.g. the first 13 steps of 99999999999999 and 99999999999998 will be identical
            // only the calculations after the final input is read will be different.
            // When we go down from 99999999999991 to 99999999999989, we will be able to use the
            // first 12 steps
            val states = mutableMapOf<String, MutableMap<Char, Int>>()
            var skiptoNextInput = false
            fun saveState() {
                if (inputIndex < 14) states[input.take(inputIndex)] = variables.toMutableMap()
            }
            fun hasState() = states.containsKey(input.take(inputIndex))
            fun getState() {
                if (hasState()) {
                    variables = states[input.take(inputIndex)]!!.toMutableMap()
                    skiptoNextInput = true
                }
                else {
                    skiptoNextInput = false
                }
            }
        }
    }
    class Input(val variable: Char) : Instruction()
    abstract class Operation(val a:Char, val b:String) : Instruction()
    class Add(a:Char, b:String) : Operation(a,b)
    class Mulitply(a:Char, b:String) : Operation(a,b)
    class Divide(a:Char, b:String) : Operation(a,b)
    class Mod(a:Char, b:String) : Operation(a,b)
    class Equals(a:Char, b:String) : Operation(a,b)

    val instructions = File("inputs/day24.txt").readLines().mapNotNull { instruction ->
        instruction.split(" ").let {
            if (it[0] == "inp") Input(it[1].first())
            else {
                val a = it[1].first()
                val b = it[2]
                when (it[0]) {
                    "add" -> Add(a, b)
                    "mul" -> Mulitply(a, b)
                    "div" -> Divide(a, b)
                    "mod" -> Mod(a, b)
                    "eql" -> Equals(a, b)
                    else -> null
                }
            }
        }
    }

    fun run(checkSerial: String): Boolean {

        Instruction.setNewSerial(checkSerial)
        // instructions are in blocks of 18, an input, then 17 operations
        (0..13).forEach {
            instructions.drop(it*18).first().process() // process input step
            if (!Instruction.hasState()) {
                // we don't have this state, so we need to process, then save
                instructions.subList(it*18+1, (it+1)*18).forEach { it.process() }
                Instruction.saveState()
            }
            else {
                Instruction.getState()
            }
        }

        return Instruction.isValidSerial()
    }

    //12934998949199
    //11711691612189

    fun part1() = run(99999999999999, 11111111111111)
    fun part2() = run(11111111111111, 12934998949199)

    fun run(from:Long, to:Long) {
        val diff = maxOf(from, to) - minOf(from, to)
        val x = diff / 1000
        println("Processing $diff steps")

        val progession = if (from > to) from downTo to else from until to
        for (i in progession) {
            if (i % x == 0L) print(".")

            val input = i.toString()
            if (!input.contains("0")) {
                if (run(input)) {
                    println(input)
                    return
                }
            }
        }
    }
}