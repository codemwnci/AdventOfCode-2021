import java.io.File

fun main() {
    println("Puzzle1: ${Day17().puzzle1()}")
    println("Puzzle2: ${Day17().puzzle2()}")
}

class Day17 {

    data class BoundingBox(val xRange: IntRange, val yRange: IntRange)
    private val target = File("inputs/day17.txt").readLines().first().drop(13)
        .split(", ").map { it.drop(2).split("..").let { (a, b) -> a.toInt()..b.toInt() }}
        .let { BoundingBox(it[0], it[1]) }


    fun puzzle1() = (1..400).flatMap { x -> (1..400).map { y -> checkStep(x, y, target) } }.fold(0) { max, step -> if (step.first) maxOf(max, step.second) else max }
    fun puzzle2() = (1..400).flatMap { x -> (-400..400).map { y -> checkStep(x, y, target).first } }.count { it }


    data class Coord(var x:Int, var y:Int)
    data class Velocity(var x:Int, var y:Int)
    private fun checkStep(xVelocity: Int, yVelocity: Int, target: BoundingBox): Pair<Boolean, Int> {
        // starting from 0,0 - step until we meet, or exceed target
        val p = Coord(0, 0)
        val v = Velocity(xVelocity, yVelocity)
        var maxHeight = 0
        var hitTarget = false
        while (p.x <= target.xRange.last && p.y >= target.yRange.first) {
            p.x += v.x      // increment positions by velocities
            p.y += v.y

            maxHeight = maxOf(p.y, maxHeight)
            if (p.x in target.xRange && p.y in target.yRange) {
                hitTarget = true
                break // no point processing more (albeit unlikely to process many more steps regardless)
            }

            // decrease velocities as per rules
            if (v.x > 0) v.x--
            v.y--
        }
        return Pair(hitTarget, maxHeight)
    }
}