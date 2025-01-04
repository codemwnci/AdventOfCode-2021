import java.io.File
import kotlin.math.abs

fun main() {
    Day19().puzzles()
}

class Day19 {
    private val file = File("inputs/day19.txt")
    private val scanners: List<Set<Point3D>> = file.readLines().splitOnBlank().map {
        it.drop(1).map { getPoint3D(it) }.toSet()
    }

    private fun getPoint3D(string: String) = string.split(",").map { it.toInt() }.let { (x,y,z) -> Point3D(x,y,z) }
    private data class Point3D(val x:Int, val y:Int, val z:Int) {
        fun face(facing: Int): Point3D = when (facing) {
                0 -> this
                1 -> Point3D(x, -y, -z)
                2 -> Point3D(x, -z, y)
                3 -> Point3D(-y, -z, x)
                4 -> Point3D(y, -z, -x)
                5 -> Point3D(-x, -z, -y)
                else -> error("Invalid facing")
            }

        fun rotate(rotating: Int): Point3D = when (rotating) {
                0 -> this
                1 -> Point3D(-y, x, z)
                2 -> Point3D(-x, -y, z)
                3 -> Point3D(y, -x, z)
                else -> error("Invalid rotation")
            }

        fun diff(other: Point3D): Point3D = Point3D(this.x - other.x, this.y - other.y, this.z - other.z)
        fun move(amount: Point3D) = Point3D(this.x + amount.x, this.y + amount.y, this.z + amount.z)
        fun manhattan(other: Point3D) = abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z)
    }
    private val ORIGIN = Point3D(0,0,0)

    private fun doesIntersect(lhs: Set<Point3D>, rhs: Set<Point3D>): Pair<Point3D, Set<Point3D>>? =
        (0 until 6).firstNotNullOfOrNull  { face -> (0 until 4).firstNotNullOfOrNull  { rotation ->
                val transform = rhs.map { it.face(face).rotate(rotation) }.toSet()
                lhs.firstNotNullOfOrNull { s1 -> transform.firstNotNullOfOrNull { s2 ->
                    val diff = s1.diff(s2)
                    val updated = transform.map { it.move(diff) }.toSet()
                    if (updated.intersect(lhs).size >= 12) diff to updated
                    else null
                } }
            }
        }


    fun puzzles() {
        val knownScanners = scanners.first().toMutableSet()
        val found = mutableSetOf(ORIGIN)
        val unmapped = ArrayDeque<Set<Point3D>>().also { it.addAll(scanners.drop(1)) }

        while (unmapped.isNotEmpty()) {
            val curr = unmapped.removeFirst()
            when (val intersect = doesIntersect(knownScanners, curr)) {
                null -> unmapped.add(curr)
                else -> {
                    knownScanners.addAll(intersect.second)
                    found.add(intersect.first)
                }
            }
        }

        println(knownScanners.size)
        println(found.toList().combinations(2).maxOf { (a, b) -> a.manhattan(b) })
    }
}

private fun List<String>.splitOnBlank(): List<List<String>> = this.split { it.isBlank() }
private fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> = fold(mutableListOf(mutableListOf<T>())) { acc, t ->
    if (predicate(t)) acc.add(mutableListOf())
    else acc.last().add(t)
    acc
}
fun <T> List<T>.combinations(size: Int): List<List<T>> = when (size) {
    0 -> listOf(listOf())
    else -> flatMapIndexed { idx, element -> drop(idx + 1).combinations(size - 1).map { listOf(element) + it } }
}